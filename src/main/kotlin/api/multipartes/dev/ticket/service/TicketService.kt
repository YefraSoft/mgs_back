package api.multipartes.dev.ticket.service

import api.multipartes.dev.enums.PaymentMethod
import api.multipartes.dev.enums.WarrantyStatus
import api.multipartes.dev.models.Sale
import api.multipartes.dev.models.Ticket
import api.multipartes.dev.models.User
import api.multipartes.dev.models.Warranty
import api.multipartes.dev.parts.repository.PartsRepository
import api.multipartes.dev.sales.repository.SalesRepository
import api.multipartes.dev.ticket.dto.*
import api.multipartes.dev.ticket.repository.TicketRepository
import api.multipartes.dev.user.repository.UserRepository
import api.multipartes.dev.warranty.repository.WarrantyRepository
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class TicketService(
    private val _repo: TicketRepository,
    private val _salesRepo: SalesRepository,
    private val _partsRepo: PartsRepository,
    private val _userRepo: UserRepository,
    private val _warrantyRepo: WarrantyRepository
) {

    @Transactional
    @CacheEvict(value = ["tickets"], allEntries = true)
    fun make(request: TicketRequest): MakeResponse {
        val ticket = buildTicketFromRequest(request)
        val savedTicket = saveTicketWithRelations(ticket, request.items)
        return MakeResponse(
            ticketFolio = savedTicket.first.folio,
            salesId = savedTicket.second
        )
    }

    @Transactional
    @CacheEvict(value = ["tickets"], allEntries = true)
    fun update(folio: String, request: UpdateTicketRequest): GetTicketResponse {
        val existingTicket = _repo.findById(folio)
            .orElseThrow { IllegalArgumentException("Ticket with folio $folio not found") }

        request.items?.let {
            validateItems(it)
            validateTicketTotals(request.total ?: existingTicket.total, it)
        }

        val updatedTicket = _repo.save(
            existingTicket.copy(
                user = request.sellerId?.let { fetchUser(it) } ?: existingTicket.user,
                total = request.total ?: existingTicket.total,
                paymentMethod = request.paymentMethod?.let { parsePaymentMethod(it) } ?: existingTicket.paymentMethod,
                items = request.items?.size?.toByte() ?: existingTicket.items,
                date = LocalDateTime.now()
            )
        )

        request.items?.let {
            _salesRepo.deleteByTicketFolio(folio)
            saveTicketItems(updatedTicket, it)
        }

        return mapToResponse(updatedTicket)
    }

    @Transactional
    @CacheEvict(value = ["tickets"], allEntries = true)
    fun delete(folio: String) {
        val ticket = _repo.findById(folio)
            .orElseThrow { IllegalArgumentException("Ticket with folio $folio not found") }

        val sales = _salesRepo.findByTicketFolio(folio)
        sales.forEach { sale ->
            sale.id?.let { _warrantyRepo.findBySaleId(it)?.let { warranty -> _warrantyRepo.delete(warranty) } }
        }

        _salesRepo.deleteByTicketFolio(folio)
        _repo.delete(ticket)
    }

    fun getByFolio(folio: String): GetTicketResponse? {
        val ticket = _repo.findById(folio).orElse(null) ?: return null
        return mapToResponse(ticket)
    }

    @Cacheable("tickets")
    fun getAll(): List<GetTicketResponse> = _repo.findAll().map { mapToResponse(it) }

    fun getByUser(userId: Int): List<GetTicketResponse> = _repo.findByUserId(userId).map { mapToResponse(it) }

    fun getTicketsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<GetTicketResponse> =
        _repo.findByDateBetween(startDate, endDate).map { mapToResponse(it) }

    private fun buildTicketFromRequest(request: TicketRequest): Ticket {
        val user = fetchUser(request.sellerId)
        val paymentMethod = parsePaymentMethod(request.paymentMethod)
        validateTicketTotals(request.total, request.items)
        validateItems(request.items)

        return Ticket(
            user = user,
            total = request.total,
            paymentMethod = paymentMethod,
            items = request.items.size.toByte()
        )
    }

    private fun saveTicketWithRelations(ticket: Ticket, items: List<TicketItem>): Pair<Ticket, List<Int>> {
        val savedTicket = _repo.save(ticket)
        val savedSalesIds = saveTicketItems(savedTicket, items)
        return savedTicket to savedSalesIds
    }

    private fun saveTicketItems(ticket: Ticket, items: List<TicketItem>): List<Int> {
        val savedSales = mutableListOf<Int>()
        items.forEach { item ->
            val sale = Sale(
                ticket = ticket,
                part = item.partId?.let {
                    _partsRepo.findById(it)
                        .orElseThrow { IllegalArgumentException("Part with ID $it not found") }
                },
                quantity = item.quantity,
                price = item.price,
                partName = item.partName
            )

            val savedSale = _salesRepo.save(sale)
            savedSale.id?.let { savedSales.add(it) }

            if (item.hasWarranty && item.warrantyExpirationDate != null) {
                _warrantyRepo.save(
                    Warranty(
                        sale = savedSale,
                        status = WarrantyStatus.ACTIVE,
                        expirationDate = item.warrantyExpirationDate
                    )
                )
            }
        }
        return savedSales
    }

    private fun fetchUser(userId: Int): User {
        return _userRepo.findById(userId)
            .orElseThrow { IllegalArgumentException("User with ID $userId not found") }
    }

    private fun parsePaymentMethod(value: String): PaymentMethod {
        return try {
            PaymentMethod.valueOf(value.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid payment method: $value")
        }
    }

    private fun mapToResponse(ticket: Ticket): GetTicketResponse {
        val sales = _salesRepo.findByTicketFolio(ticket.folio)
        val salesRes = sales.map {
            SaleResponse(
                id = it.id,
                partId = it.part?.id,
                partName = it.partName ?: it.part?.name,
                quantity = it.quantity,
                price = it.price
            )
        }

        val warranties = sales.mapNotNull { sale ->
            sale.id?.let { _warrantyRepo.findBySaleId(it) }
        }

        val warrantiesRes = warranties.map {
            WarrantyResponse(
                id = it.id,
                status = it.status.toString(),
                expirationDate = it.expirationDate
            )
        }

        return GetTicketResponse(
            folio = ticket.folio,
            userId = ticket.user.id!!,
            userName = ticket.user.name,
            total = ticket.total,
            paymentMethod = ticket.paymentMethod.toString(),
            items = ticket.items,
            date = ticket.date,
            sales = salesRes,
            warranties = warrantiesRes
        )
    }

    private fun validateTicketTotals(total: BigDecimal, items: List<TicketItem>) {
        val sum = items.fold(BigDecimal.ZERO) { acc, item ->
            val quantityMultiplier = BigDecimal(item.quantity.toInt())
            acc + item.price.multiply(quantityMultiplier)
        }

        if (sum.compareTo(total) != 0) {
            throw IllegalArgumentException("Total mismatch: items sum $sum vs ticket total $total")
        }
    }

    private fun validateItems(items: List<TicketItem>) {
        if (items.isEmpty()) throw IllegalArgumentException("Ticket requires at least one item")

        items.forEach { item ->
            if (item.partId == null && item.partName == null) {
                throw IllegalArgumentException("Each item requires either partId or partName")
            }
            if (item.quantity <= 0) {
                throw IllegalArgumentException("Item quantity must be greater than 0")
            }
        }
    }
}