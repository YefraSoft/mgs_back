package api.multipartes.dev.reports.adquisitionPurchase.controller

import api.multipartes.dev.reports.adquisitionPurchase.dtos.AmountAcquisitionsPurchasesDto
import api.multipartes.dev.reports.adquisitionPurchase.service.AcquisitionPurchaseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/monthly-report")
class AcquisitionPurchaseController(private val _service: AcquisitionPurchaseService) {

    @GetMapping
    fun monthlyReport(): ResponseEntity<List<AmountAcquisitionsPurchasesDto>> {
        val result = _service.getMonthlyReport()
        return if (result.isEmpty())
            ResponseEntity.noContent().build()
        else
            ResponseEntity.ok(result)
    }

}