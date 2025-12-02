package api.multipartes.dev.ticket.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class GetTicketResponse(
    val folio: String,
    val userId: Int?,
    val userName: String,
    val total: BigDecimal,
    val paymentMethod: String,
    val items: Byte,
    val date: LocalDateTime,
    val sales: List<SaleResponse>,
    val warranties: List<WarrantyResponse>
)

data class MakeResponse(
    val ticketFolio: String,
    val salesId: List<Int>
)

