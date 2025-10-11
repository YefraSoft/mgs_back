package api.multipartes.dev.dtos

import java.math.BigDecimal
import java.time.LocalDateTime

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(val role: String, val token: String)

data class RegisterRequest(
    val name: String,
    val username: String,
    val password: String,
    val roleId: Byte = 1
)


data class CreateTicketRequest(
    val userId: Int,
    val paymentMethod: String,
    val items: List<TicketItemRequest>
)

data class TicketItemRequest(
    val partId: Int,
    val quantity: Byte,
    val price: BigDecimal
)

data class TicketResponse(
    val folio: String,
    val userId: Int,
    val userName: String,
    val total: BigDecimal,
    val paymentMethod: String,
    val items: Byte,
    val date: LocalDateTime,
    val sales: List<SaleResponse>
)

data class SaleResponse(
    val id: Int,
    val ticketFolio: String,
    val partId: Int,
    val partName: String,
    val quantity: Byte,
    val price: BigDecimal
)

data class PartResponse(
    val id: Int,
    val code: String?,
    val name: String,
    val side: String,
    val categoryId: Int,
    val categoryName: String,
    val color: String?,
    val price: BigDecimal,
    val quantity: Byte
)
