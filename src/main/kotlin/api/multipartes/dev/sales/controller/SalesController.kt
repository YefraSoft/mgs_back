package api.multipartes.dev.sales.controller

import api.multipartes.dev.models.Sale
import api.multipartes.dev.sales.service.SalesService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sales")
class SalesController(private val _service: SalesService) {
    @GetMapping
    fun getAll(): ResponseEntity<List<Sale>> {
        return ResponseEntity.ok(_service.getAll())
    }

    @GetMapping("/{id}")
    fun getSaleById(@PathVariable id: Int): ResponseEntity<Sale> {
        val sale = _service.findById(id)
        return if (sale.isPresent) {
            ResponseEntity.ok(sale.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/by-part/{partId}")
    fun getSalesByPart(@PathVariable partId: Int): ResponseEntity<List<Sale>> {
        return ResponseEntity.ok(_service.getByPart(partId))
    }

    @GetMapping("/by-ticket/{ticketFolio}")
    fun getSalesByTicket(@PathVariable ticketFolio: String): ResponseEntity<List<Sale>> {
        return ResponseEntity.ok(_service.getByTicked(ticketFolio))
    }

}