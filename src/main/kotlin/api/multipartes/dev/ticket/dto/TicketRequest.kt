package api.multipartes.dev.ticket.dto

import java.math.BigDecimal

data class TicketRequest(
    val sellerId: Int,
    val paymentMethod: String,
    val total: BigDecimal,
    val items: List<TicketItem>
)
