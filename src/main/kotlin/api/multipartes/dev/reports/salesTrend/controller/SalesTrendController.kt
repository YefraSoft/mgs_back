package api.multipartes.dev.reports.salesTrend.controller

import api.multipartes.dev.reports.salesTrend.dto.SalesTrend
import api.multipartes.dev.reports.salesTrend.service.SalesTrendService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sales-trend")
class SalesTrendController(private val _service: SalesTrendService) {

    @GetMapping("/monthly")
    fun getMonthlySalesTrend(): ResponseEntity<List<SalesTrend>> {
        val result = _service.getMonthlySalesTrend()
        return ResponseEntity.ok(result)
    }
}