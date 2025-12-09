package api.multipartes.dev.ticket.dto

import java.math.BigDecimal

data class SalesTrend(
    val periodo: String,
    val totalVendido: BigDecimal,
    val cantidadDeVentas: Long
)
