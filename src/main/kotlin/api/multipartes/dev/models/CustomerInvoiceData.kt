package api.multipartes.dev.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(
    name = "customer_invoice_data", indexes = [
        Index(name = "idx_customer_id", columnList = "customer_id"),
        Index(name = "idx_email", columnList = "email")
    ]
)
data class CustomerInvoiceData(
    @Id
    @Column(length = 13)
    val rfc: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    val customer: Customer,

    @Column(name = "business_name", nullable = false)
    @NotBlank(message = "Business name is required")
    val businessName: String,

    @Column(nullable = false)
    @NotBlank(message = "Address is required")
    val address: String,

    @Column(name = "postal_code", nullable = false, length = 6)
    @NotBlank(message = "Postal code is required")
    val postalCode: String,

    @Column(name = "tax_regime", nullable = false, length = 50)
    @NotBlank(message = "Tax regime is required")
    val taxRegime: String,

    @Column(name = "invoice_use", nullable = false, length = 50)
    @NotBlank(message = "Invoice use is required")
    val invoiceUse: String,

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Email is required")
    val email: String
)