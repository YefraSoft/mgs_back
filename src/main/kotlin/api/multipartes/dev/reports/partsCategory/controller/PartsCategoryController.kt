package api.multipartes.dev.reports.partsCategory.controller

import api.multipartes.dev.parts.dto.PartsCategoryDto
import api.multipartes.dev.reports.partsCategory.service.PartsCategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/category-trend")
class PartsCategoryController(private val _service: PartsCategoryService) {

    @GetMapping
    fun getCategoryTrend(): ResponseEntity<List<PartsCategoryDto>> {
        val result = _service.getDistribution()
        return if (result.isEmpty())
            ResponseEntity.noContent().build()
        else
            ResponseEntity.ok(result)
    }
}