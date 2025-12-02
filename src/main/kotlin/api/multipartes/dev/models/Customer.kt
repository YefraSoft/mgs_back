package api.multipartes.dev.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(
    name = "customer", indexes = [
        Index(name = "idx_phone", columnList = "phone"),
        Index(name = "idx_rfc", columnList = "rfc")
    ]
)
data class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Customer name is required")
    val name: String,

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Phone is required")
    val phone: String,

    @Column(length = 13)
    val rfc: String? = null,

    @OneToOne(mappedBy = "customer", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val invoiceData: CustomerInvoiceData? = null
)
