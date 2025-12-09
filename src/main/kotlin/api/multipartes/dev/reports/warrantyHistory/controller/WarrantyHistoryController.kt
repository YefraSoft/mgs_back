package api.multipartes.dev.reports.warrantyHistory.controller

import api.multipartes.dev.reports.warrantyHistory.dto.WarrantyHistoryDto
import api.multipartes.dev.reports.warrantyHistory.service.WarrantyHistoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/warranty-history")
class WarrantyHistoryController(private val _service: WarrantyHistoryService) {

    @GetMapping
    fun getWarrantyHistory(): ResponseEntity<List<WarrantyHistoryDto>> {
        val result = _service.getWarrantyHistory()
        return if (result.isEmpty())
            ResponseEntity.noContent().build()
        else
            ResponseEntity.ok(result)
    }

}