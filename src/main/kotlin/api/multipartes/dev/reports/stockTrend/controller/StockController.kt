package api.multipartes.dev.reports.stockTrend.controller

import api.multipartes.dev.parts.dto.StockDto
import api.multipartes.dev.reports.stockTrend.service.StockService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/stock-trend")
class StockController(private val _service: StockService) {

    @GetMapping("/{threshold}")
    fun getLowStock(@PathVariable threshold: Int): ResponseEntity<List<StockDto>> {
        val result = _service.getLowStock(threshold)
        return if (result.isEmpty())
            ResponseEntity.noContent().build()
        else
            ResponseEntity.ok(result)
    }

}