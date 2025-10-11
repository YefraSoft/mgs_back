package api.multipartes.dev.endPoints.sales

import api.multipartes.dev.dtos.SaleResponse
import api.multipartes.dev.models.Sale
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sales")
class SalesController(private val saleService: SalesService) {

    @GetMapping
    fun getAllSales(): ResponseEntity<List<SaleResponse>> {
        return ResponseEntity.ok(saleService.getAllSales())
    }

    @GetMapping("/{id}")
    fun getSaleById(@PathVariable id: Int): ResponseEntity<Sale> {
        val sale = saleService.findById(id)
        return if (sale.isPresent) {
            ResponseEntity.ok(sale.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/search/by-part/{partId}")
    fun getSalesByPart(@PathVariable partId: Int): ResponseEntity<List<SaleResponse>> {
        return ResponseEntity.ok(saleService.getSalesByPart(partId))
    }

    @GetMapping("/search/by-ticket/{ticketFolio}")
    fun getSalesByTicket(@PathVariable ticketFolio: String): ResponseEntity<List<SaleResponse>> {
        return ResponseEntity.ok(saleService.getSalesByTicket(ticketFolio))
    }
}