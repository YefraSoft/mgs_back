package api.multipartes.dev.endPoints.sales

import api.multipartes.dev.dtos.SaleResponse
import api.multipartes.dev.models.Sale
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class SalesService(
    private val saleRepository: SalesRepo
) {

    fun findById(id: Int): Optional<Sale> {
        return saleRepository.findById(id)
    }

    @Cacheable("sales")
    fun getAllSales(): List<SaleResponse> {
        return saleRepository.findAll().map { sale ->
            SaleResponse(
                id = sale.id!!,
                ticketFolio = sale.ticket.folio,
                partId = sale.part.id!!,
                partName = sale.part.name,
                quantity = sale.quantity,
                price = sale.price
            )
        }
    }

    fun getSalesByPart(partId: Int): List<SaleResponse> {
        return saleRepository.findByPartId(partId).map { sale ->
            SaleResponse(
                id = sale.id!!,
                ticketFolio = sale.ticket.folio,
                partId = sale.part.id!!,
                partName = sale.part.name,
                quantity = sale.quantity,
                price = sale.price
            )
        }
    }

    fun getSalesByTicket(ticketFolio: String): List<SaleResponse> {
        return saleRepository.findByTicketFolio(ticketFolio).map { sale ->
            SaleResponse(
                id = sale.id!!,
                ticketFolio = sale.ticket.folio,
                partId = sale.part.id!!,
                partName = sale.part.name,
                quantity = sale.quantity,
                price = sale.price
            )
        }
    }
}