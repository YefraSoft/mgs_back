package api.multipartes.dev.ticket.dto

import java.math.BigDecimal
import java.time.LocalDate

data class TicketItem(
    val partId: Int? = null,
    val quantity: Byte,
    val price: BigDecimal,
    val partName: String? = null,
    val hasWarranty: Boolean = false,
    val warrantyExpirationDate: LocalDate? = null
)
