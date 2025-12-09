package api.multipartes.dev.reports.expenses.dto

import java.math.BigDecimal

data class ExpensesReportDto(
    val tipoGasto: String,
    val monto: BigDecimal,
    val fechaDePago: String?
)
