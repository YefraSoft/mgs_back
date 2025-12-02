package api.multipartes.dev.ticket.dto

import java.time.LocalDate

data class WarrantyResponse(
    val id: Int?,
    val status: String,
    val expirationDate: LocalDate?
)
