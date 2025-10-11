package api.multipartes.dev.models

import jakarta.persistence.*

@Entity
@Table(name = "customer_invoice_data")
data class CustomerInvoiceData(
    @Id
    @Column(length = 13)
    val rfc: String,

    @Column(nullable = false)
    val address: String,

    @Column(name = "postal_code", nullable = false, length = 6)
    val postalCode: String,

    @Column(name = "tax_regime", nullable = false, length = 50)
    val taxRegime: String,

    @Column(name = "invoice_use", nullable = false, length = 50)
    val invoiceUse: String,

    @Column(nullable = false, length = 100)
    val email: String
)
