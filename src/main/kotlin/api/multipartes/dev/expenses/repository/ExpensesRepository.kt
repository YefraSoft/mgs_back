package api.multipartes.dev.expenses.repository

import api.multipartes.dev.models.Expense
import api.multipartes.dev.reports.expenses.dto.ExpensesReportDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ExpensesRepository : JpaRepository<Expense, Int> {

    @Query(
        """
        SELECT category as tipoGasto, SUM(amount) as monto, DATE_FORMAT(payment_at, '%Y-%M') as fechaDePago
        FROM expenses
        GROUP BY category, fechaDePago order by fechaDePago;
        """, nativeQuery = true
    )
    fun getExpenseReport(): List<ExpensesReportDto>

}