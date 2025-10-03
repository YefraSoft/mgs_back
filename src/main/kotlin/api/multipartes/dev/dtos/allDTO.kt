package api.multipartes.dev.dtos

import java.math.BigDecimal
import java.time.LocalDateTime

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(val role: String, val token: String)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val roleId: Byte = 1
)

data class SaleRequest(
    val partId: Int,
    val customPartName: String?,
    val quantity: Byte,
    val price: BigDecimal,
    val paymentMethod: String
)

data class SaleResponse(
    val id: Int,
    val partId: Int,
    val customPartName: String?,
    val quantity: Short,
    val price: BigDecimal,
    val paymentMethod: String?,
    val createdAt: LocalDateTime
)
