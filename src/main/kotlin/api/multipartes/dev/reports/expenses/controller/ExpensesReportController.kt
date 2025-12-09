package api.multipartes.dev.reports.expenses.controller

import api.multipartes.dev.reports.expenses.dto.ExpensesReportDto
import api.multipartes.dev.reports.expenses.service.ExpensesReportService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/expenses-trend")
class ExpensesReportController(private val _service: ExpensesReportService) {

    @GetMapping
    fun getExpensesReport(): ResponseEntity<List<ExpensesReportDto>> {
        val result = _service.getExpensesReport()
        return if (result.isEmpty())
            ResponseEntity.noContent().build()
        else
            ResponseEntity.ok(result)
    }

}