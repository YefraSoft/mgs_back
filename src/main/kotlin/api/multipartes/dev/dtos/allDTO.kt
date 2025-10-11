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

// ========== BRANDS DTOs ==========
data class CreateBrandRequest(
    val name: String
)

data class UpdateBrandRequest(
    val name: String
)

data class BrandResponse(
    val id: Int,
    val name: String
)

// ========== MODELS DTOs ==========
data class CreateModelRequest(
    val brandId: Int,
    val serialNumber: String?,
    val name: String,
    val year: Int?,
    val transmission: String?, // "AUTOMATIC" o "STANDARD"
    val engine: String,
    val vehicleClass: String
)

data class UpdateModelRequest(
    val brandId: Int,
    val serialNumber: String?,
    val name: String,
    val year: Int?,
    val transmission: String?, // "AUTOMATIC" o "STANDARD"
    val engine: String,
    val vehicleClass: String
)

data class ModelResponse(
    val id: Int,
    val brandId: Int,
    val brandName: String,
    val serialNumber: String?,
    val name: String,
    val year: Int?,
    val transmission: String?,
    val engine: String,
    val vehicleClass: String
)

// ========== CUSTOMER DTOs ==========
data class CreateCustomerRequest(
    val name: String,
    val phone: String,
    val rfc: String?
)

data class UpdateCustomerRequest(
    val name: String,
    val phone: String,
    val rfc: String?
)

data class CustomerResponse(
    val id: Int,
    val name: String,
    val phone: String,
    val rfc: String?
)

// ========== CUSTOMER ISSUES DTOs ==========
data class CreateCustomerIssueRequest(
    val problem: String,
    val customerId: Int?
)

data class UpdateCustomerIssueRequest(
    val problem: String,
    val status: String, // "ATTENDED", "REJECTED", "NOT_FOUND", "PENDING"
    val customerId: Int?
)

data class CustomerIssueResponse(
    val id: Int,
    val problem: String,
    val status: String,
    val createdAt: LocalDateTime,
    val customerId: Int?,
    val customerName: String?
)
