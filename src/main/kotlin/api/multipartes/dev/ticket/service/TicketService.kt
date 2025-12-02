package api.multipartes.dev.ticket.service

import api.multipartes.dev.enums.PaymentMethod
import api.multipartes.dev.enums.WarrantyStatus
import api.multipartes.dev.models.Sale
import api.multipartes.dev.models.Ticket
import api.multipartes.dev.models.Warranty
import api.multipartes.dev.parts.repository.PartsRepository
import api.multipartes.dev.sales.repository.SalesRepository
import api.multipartes.dev.ticket.dto.*
import api.multipartes.dev.ticket.repository.TicketRepository
import api.multipartes.dev.user.repository.UserRepository
import api.multipartes.dev.warranty.repository.WarrantyRepository
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
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
    fun make(request: TicketRequest): MakeResponse {

        val user = _userRepo.findById(request.sellerId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val savedSales = mutableListOf<Int>()

        val paymentMethod = try {
            PaymentMethod.valueOf(request.paymentMethod.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid payment method: ${request.paymentMethod}")
        }

        val ticket = Ticket(
            user = user,
            total = request.total,
            paymentMethod = paymentMethod,
            items = request.items.size.toByte()
        )
        val savedTicket = _repo.save(ticket)

        request.items.forEach { item ->
            if (item.partId == null && item.partName == null)
                throw IllegalArgumentException("Each item requires either partId or partName")

            val sale = Sale(
                ticket = savedTicket,
                part = item.partId?.let { _partsRepo.findById(it).orElse(null) },
                price = item.price,
                quantity = item.quantity,
                partName = item.partName
            )
            val savedSale = try {
                _salesRepo.save(sale)
            } catch (e: Exception) {
                throw IllegalStateException("No se pudo guardar la venta: ${e.message}", e)
            }
            savedSale.id?.let { savedSales.add(it) }
            if (item.hasWarranty && item.warrantyExpirationDate != null) {
                val warranty = Warranty(
                    sale = savedSale,
                    status = WarrantyStatus.ACTIVE,
                    expirationDate = item.warrantyExpirationDate
                )
                _warrantyRepo.save(warranty)
            }
        }

        return MakeResponse(
            ticketFolio = savedTicket.folio,
            salesId = savedSales
        )
    }

    fun getByFolio(folio: String): GetTicketResponse? {
        val ticket = _repo.findById(folio).orElse(null) ?: return null
        val sales = _salesRepo.findByTicketFolio(folio)
        val salesRes = sales.map {
            SaleResponse(
                id = it.id,
                partId = it.id,
                partName = it.partName,
                quantity = it.quantity,
                price = it.price
            )
        }
        val warranties = mutableListOf<Warranty>()

        sales.forEach { sale ->
            sale.id?.let { _warrantyRepo.findBySaleId(it)?.let { id -> warranties.add(id) } }
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

    @Cacheable("tickets")
    fun getAll(): List<GetTicketResponse> {
        return _repo.findAll().map { ticket ->
            val sales = _salesRepo.findByTicketFolio(ticket.folio)
            val salesRes = sales.map {
                SaleResponse(
                    id = it.id,
                    partId = it.id,
                    partName = it.partName,
                    quantity = it.quantity,
                    price = it.price
                )
            }
            val warranties = mutableListOf<Warranty>()
            sales.forEach { sale ->
                sale.id?.let { _warrantyRepo.findBySaleId(it)?.let { id -> warranties.add(id) } }
            }
            val warrantiesRes = warranties.map {
                WarrantyResponse(
                    id = it.id,
                    status = it.status.toString(),
                    expirationDate = it.expirationDate
                )
            }
            GetTicketResponse(
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
    }

    fun getByUser(userId: Int): List<GetTicketResponse> {
        return _repo.findByUserId(userId).map { ticket ->
            val sales = _salesRepo.findByTicketFolio(ticket.folio)
            val salesRes = sales.map {
                SaleResponse(
                    id = it.id,
                    partId = it.id,
                    partName = it.partName,
                    quantity = it.quantity,
                    price = it.price
                )
            }
            val warranties = mutableListOf<WarrantyResponse>()
            GetTicketResponse(
                folio = ticket.folio,
                userId = ticket.user.id!!,
                userName = ticket.user.name,
                total = ticket.total,
                paymentMethod = ticket.paymentMethod.toString(),
                items = ticket.items,
                date = ticket.date,
                sales = salesRes,
                warranties = warranties
            )
        }
    }

    fun getTicketsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<GetTicketResponse> {
        return _repo.findByDateBetween(startDate, endDate).map { ticket ->
            val sales = _salesRepo.findByTicketFolio(ticket.folio)
            val salesRes = sales.map {
                SaleResponse(
                    id = it.id,
                    partId = it.id,
                    partName = it.partName,
                    quantity = it.quantity,
                    price = it.price
                )
            }
            val warranties = mutableListOf<WarrantyResponse>()
            GetTicketResponse(
                folio = ticket.folio,
                userId = ticket.user.id!!,
                userName = ticket.user.name,
                total = ticket.total,
                paymentMethod = ticket.paymentMethod.toString(),
                items = ticket.items,
                date = ticket.date,
                sales = salesRes,
                warranties = warranties
            )
        }
    }
}