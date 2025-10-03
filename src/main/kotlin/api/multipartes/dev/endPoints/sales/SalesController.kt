package api.multipartes.dev.endPoints.sales

import api.multipartes.dev.dtos.SaleRequest
import api.multipartes.dev.models.Sale
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*


@RestController
@RequestMapping("/api/sales")
class SalesController(private val saleService: SalesService) {

    @PostMapping
    fun createSale(@RequestBody saleRequest: SaleRequest): ResponseEntity<Sale> {
        val sale = saleService.createSale(saleRequest)
        return ok(sale)
    }

    @GetMapping
    fun getAllSales(): ResponseEntity<List<Sale>> {
        println("Here")
        return ok(saleService.getAllSales())
    }

    @GetMapping("/{id}")
    fun getSaleById(@PathVariable id: Int): ResponseEntity<Optional<Sale>> {
        val sale = saleService.findById(id)
        return sale.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/search/by-part")
    fun getSalesByPart(@PathVariable partId: Int): ResponseEntity<List<Sale>> {
        return ok(saleService.getSalesByPart(partId))
    }

    @GetMapping("/search/by-date")
    fun getSalesByDateRange(
        @RequestParam startDate: String,
        @RequestParam endDate: String
    ): ResponseEntity<List<Sale>> {
        val start = LocalDateTime.parse(startDate)
        val end = LocalDateTime.parse(endDate)
        return ok(saleService.getSalesByDateRange(start, end))
    }

    @PutMapping("/{id}")
    fun updateSale(@PathVariable id: Int, @RequestBody saleRequest: SaleRequest): ResponseEntity<Sale> {
        val updatedSale = saleService.updateSale(id, saleRequest)
        return ok(updatedSale)
    }

    @DeleteMapping("/{id}")
    fun deleteSale(@PathVariable id: Int): ResponseEntity<Void> {
        saleService.deleteSale(id)
        return noContent().build()
    }
}