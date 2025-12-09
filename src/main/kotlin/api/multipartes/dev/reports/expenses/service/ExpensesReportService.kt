package api.multipartes.dev.reports.expenses.service

import api.multipartes.dev.expenses.repository.ExpensesRepository
import api.multipartes.dev.reports.expenses.dto.ExpensesReportDto
import org.springframework.stereotype.Service

@Service
class ExpensesReportService(private val _repo: ExpensesRepository) {

    fun getExpensesReport(): List<ExpensesReportDto> {
        return _repo.getExpenseReport()
    }

}