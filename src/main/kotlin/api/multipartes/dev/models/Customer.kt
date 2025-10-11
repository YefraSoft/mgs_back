package api.multipartes.dev.models

import jakarta.persistence.*

@Entity
@Table(name = "customer")
data class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false, length = 100)
    val name: String,

    @Column(nullable = false, length = 20)
    val phone: String,

    @ManyToOne
    @JoinColumn(name = "rfc", referencedColumnName = "rfc")
    val invoiceData: CustomerInvoiceData? = null
)
