package api.multipartes.dev.ticket.dto

import java.math.BigDecimal

data class TicketRequest(
    val sellerId: Int,
    val paymentMethod: String,
    val total: BigDecimal,
    val items: List<TicketItem>
)

data class UpdateTicketRequest(
    val sellerId: Int? = null,
    val paymentMethod: String? = null,
    val total: BigDecimal? = null,
    val items: List<TicketItem>? = null
)
