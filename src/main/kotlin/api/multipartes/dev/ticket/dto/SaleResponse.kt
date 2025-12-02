package api.multipartes.dev.ticket.dto

import java.math.BigDecimal

data class SaleResponse(

    val id: Int?,
    val partId: Int?,
    val partName: String?,
    val quantity: Byte,
    val price: BigDecimal
)
