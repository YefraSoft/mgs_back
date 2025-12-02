package api.multipartes.dev.dtos

import api.multipartes.dev.models.Warranty
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDateTime

data class LoginRequest(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, numbers, underscores and hyphens")
    val username: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String
)

data class LoginResponse(val role: String, val token: String)

data class RegisterRequest(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    val name: String,

    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, numbers, underscores and hyphens")
    val username: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    val password: String,

    @field:Min(value = 1, message = "Role ID must be at least 1")
    @field:Max(value = 5, message = "Invalid role ID")
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
    val sales: List<SaleResponse>,
    val warranties: List<Warranty>
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
    val category: String,
    val color: String?,
    val price: BigDecimal,
    val quantity: Byte
)

// ========== BRANDS DTOs ==========
data class CreateBrandRequest(
    @field:NotBlank(message = "Brand name is required")
    @field:Size(min = 2, max = 50, message = "Brand name must be between 2 and 50 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9 -]+$", message = "Brand name can only contain letters, numbers, spaces and hyphens")
    val name: String
)

data class UpdateBrandRequest(
    @field:NotBlank(message = "Brand name is required")
    @field:Size(min = 2, max = 50, message = "Brand name must be between 2 and 50 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9 -]+$", message = "Brand name can only contain letters, numbers, spaces and hyphens")
    val name: String
)

data class BrandResponse(
    val id: Int,
    val name: String
)

// ========== MODELS DTOs ==========
data class CreateModelRequest(
    @field:Min(value = 1, message = "Brand ID must be a positive number")
    val brandId: Int,

    @field:Size(max = 100, message = "Serial number must not exceed 100 characters")
    val serialNumber: String?,

    @field:NotBlank(message = "Model name is required")
    @field:Size(min = 1, max = 50, message = "Model name must be between 1 and 50 characters")
    val name: String,

    @field:Min(value = 1900, message = "Year must be 1900 or later")
    @field:Max(value = 2100, message = "Year must be 2100 or earlier")
    val year: Int?,

    @field:Pattern(regexp = "^(AUTOMATIC|STANDARD)?$", message = "Transmission must be AUTOMATIC or STANDARD")
    val transmission: String?,

    @field:NotBlank(message = "Engine is required")
    @field:Size(min = 1, max = 50, message = "Engine must be between 1 and 50 characters")
    val engine: String,

    @field:NotBlank(message = "Vehicle class is required")
    @field:Size(min = 1, max = 50, message = "Vehicle class must be between 1 and 50 characters")
    val vehicleClass: String
)

data class UpdateModelRequest(
    @field:Min(value = 1, message = "Brand ID must be a positive number")
    val brandId: Int,

    @field:Size(max = 100, message = "Serial number must not exceed 100 characters")
    val serialNumber: String?,

    @field:NotBlank(message = "Model name is required")
    @field:Size(min = 1, max = 50, message = "Model name must be between 1 and 50 characters")
    val name: String,

    @field:Min(value = 1900, message = "Year must be 1900 or later")
    @field:Max(value = 2100, message = "Year must be 2100 or earlier")
    val year: Int?,

    @field:Pattern(regexp = "^(AUTOMATIC|STANDARD)?$", message = "Transmission must be AUTOMATIC or STANDARD")
    val transmission: String?,

    @field:NotBlank(message = "Engine is required")
    @field:Size(min = 1, max = 50, message = "Engine must be between 1 and 50 characters")
    val engine: String,

    @field:NotBlank(message = "Vehicle class is required")
    @field:Size(min = 1, max = 50, message = "Vehicle class must be between 1 and 50 characters")
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
    @field:NotBlank(message = "Customer name is required")
    @field:Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    val name: String,

    @field:NotBlank(message = "Phone is required")
    @field:Pattern(regexp = "^[0-9]{10,20}$", message = "Phone must be between 10 and 20 digits")
    val phone: String,

    @field:Size(min = 12, max = 13, message = "RFC must be 12 or 13 characters")
    @field:Pattern(regexp = "^[A-Z&Ñ]{3,4}[0-9]{6}[A-Z0-9]{3}$", message = "Invalid RFC format")
    val rfc: String?
)

data class UpdateCustomerRequest(
    @field:NotBlank(message = "Customer name is required")
    @field:Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    val name: String,

    @field:NotBlank(message = "Phone is required")
    @field:Pattern(regexp = "^[0-9]{10,20}$", message = "Phone must be between 10 and 20 digits")
    val phone: String,

    @field:Size(min = 12, max = 13, message = "RFC must be 12 or 13 characters")
    @field:Pattern(regexp = "^[A-Z&Ñ]{3,4}[0-9]{6}[A-Z0-9]{3}$", message = "Invalid RFC format")
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
    @field:NotBlank(message = "Problem description is required")
    @field:Size(min = 10, max = 255, message = "Problem description must be between 10 and 255 characters")
    val problem: String,

    @field:Min(value = 1, message = "Customer ID must be a positive number")
    val customerId: Int?
)

data class UpdateCustomerIssueRequest(
    @field:NotBlank(message = "Problem description is required")
    @field:Size(min = 10, max = 255, message = "Problem description must be between 10 and 255 characters")
    val problem: String,

    @field:NotBlank(message = "Status is required")
    @field:Pattern(regexp = "^(ATTENDED|REJECTED|NOT_FOUND|PENDING)$", message = "Status must be ATTENDED, REJECTED, NOT_FOUND, or PENDING")
    val status: String,

    @field:Min(value = 1, message = "Customer ID must be a positive number")
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
