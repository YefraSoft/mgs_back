package api.multipartes.dev.reports.salesTrend.dto

import java.math.BigDecimal

data class SalesTrend(
    val periodo: String,
    val totalVendido: BigDecimal,
    val cantidadDeVentas: Long
)
