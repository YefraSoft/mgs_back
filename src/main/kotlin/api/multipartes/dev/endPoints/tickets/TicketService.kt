package api.multipartes.dev.endPoints.tickets

import api.multipartes.dev.dtos.CreateTicketRequest
import api.multipartes.dev.dtos.SaleResponse
import api.multipartes.dev.dtos.TicketResponse
import api.multipartes.dev.endPoints.parts.PartsRepo
import api.multipartes.dev.enums.PaymentMethod
import api.multipartes.dev.models.Sale
import api.multipartes.dev.models.Ticket
import api.multipartes.dev.repositories.TicketRepository
import api.multipartes.dev.endPoints.sales.SalesRepo
import api.multipartes.dev.user.UserRepo
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class TicketService(
    private val ticketRepository: TicketRepository,
    private val salesRepository: SalesRepo,
    private val partsRepo: PartsRepo,
    private val userRepo: UserRepo
) {

    @Transactional
    fun createTicket(request: CreateTicketRequest): TicketResponse {

        val user = userRepo.findById(request.userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        var total = BigDecimal.ZERO
        val salesList = mutableListOf<Sale>()


        val paymentMethodEnum = try {
            PaymentMethod.valueOf(request.paymentMethod.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid payment method: ${request.paymentMethod}")
        }

        val ticket = Ticket(
            user = user,
            total = BigDecimal.ZERO,
            paymentMethod = paymentMethodEnum,
            items = request.items.size.toByte()
        )
        val savedTicket = ticketRepository.save(ticket)

 
        request.items.forEach { item ->
            val part = partsRepo.findById(item.partId)
                .orElseThrow { IllegalArgumentException("Part with id ${item.partId} not found") }

            val sale = Sale(
                ticket = savedTicket,
                part = part,
                quantity = item.quantity,
                price = item.price
            )
            salesList.add(salesRepository.save(sale))
            total = total.add(item.price.multiply(BigDecimal(item.quantity.toInt())))
        }

        val updatedTicket = savedTicket.copy(total = total)
        ticketRepository.save(updatedTicket)

        return TicketResponse(
            folio = updatedTicket.folio,
            userId = user.id!!,
            userName = user.name,
            total = total,
            paymentMethod = updatedTicket.paymentMethod.name,
            items = updatedTicket.items,
            date = updatedTicket.date,
            sales = salesList.map { sale ->
                SaleResponse(
                    id = sale.id!!,
                    ticketFolio = sale.ticket.folio,
                    partId = sale.part.id!!,
                    partName = sale.part.name,
                    quantity = sale.quantity,
                    price = sale.price
                )
            }
        )
    }

    fun getTicketByFolio(folio: String): TicketResponse? {
        val ticket = ticketRepository.findById(folio).orElse(null) ?: return null
        val sales = salesRepository.findByTicketFolio(folio)

        return TicketResponse(
            folio = ticket.folio,
            userId = ticket.user.id!!,
            userName = ticket.user.name,
            total = ticket.total,
            paymentMethod = ticket.paymentMethod.name,
            items = ticket.items,
            date = ticket.date,
            sales = sales.map { sale ->
                SaleResponse(
                    id = sale.id!!,
                    ticketFolio = sale.ticket.folio,
                    partId = sale.part.id!!,
                    partName = sale.part.name,
                    quantity = sale.quantity,
                    price = sale.price
                )
            }
        )
    }

    @Cacheable("tickets")
    fun getAllTickets(): List<TicketResponse> {
        return ticketRepository.findAll().map { ticket ->
            val sales = salesRepository.findByTicketFolio(ticket.folio)
            TicketResponse(
                folio = ticket.folio,
                userId = ticket.user.id!!,
                userName = ticket.user.name,
                total = ticket.total,
                paymentMethod = ticket.paymentMethod.name,
                items = ticket.items,
                date = ticket.date,
                sales = sales.map { sale ->
                    SaleResponse(
                        id = sale.id!!,
                        ticketFolio = sale.ticket.folio,
                        partId = sale.part.id!!,
                        partName = sale.part.name,
                        quantity = sale.quantity,
                        price = sale.price
                    )
                }
            )
        }
    }

    fun getTicketsByUser(userId: Int): List<TicketResponse> {
        return ticketRepository.findByUserId(userId).map { ticket ->
            val sales = salesRepository.findByTicketFolio(ticket.folio)
            TicketResponse(
                folio = ticket.folio,
                userId = ticket.user.id!!,
                userName = ticket.user.name,
                total = ticket.total,
                paymentMethod = ticket.paymentMethod.name,
                items = ticket.items,
                date = ticket.date,
                sales = sales.map { sale ->
                    SaleResponse(
                        id = sale.id!!,
                        ticketFolio = sale.ticket.folio,
                        partId = sale.part.id!!,
                        partName = sale.part.name,
                        quantity = sale.quantity,
                        price = sale.price
                    )
                }
            )
        }
    }

    fun getTicketsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<TicketResponse> {
        return ticketRepository.findByDateBetween(startDate, endDate).map { ticket ->
            val sales = salesRepository.findByTicketFolio(ticket.folio)
            TicketResponse(
                folio = ticket.folio,
                userId = ticket.user.id!!,
                userName = ticket.user.name,
                total = ticket.total,
                paymentMethod = ticket.paymentMethod.name,
                items = ticket.items,
                date = ticket.date,
                sales = sales.map { sale ->
                    SaleResponse(
                        id = sale.id!!,
                        ticketFolio = sale.ticket.folio,
                        partId = sale.part.id!!,
                        partName = sale.part.name,
                        quantity = sale.quantity,
                        price = sale.price
                    )
                }
            )
        }
    }
}
