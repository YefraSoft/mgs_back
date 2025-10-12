package api.multipartes.dev.models

import api.multipartes.dev.enums.WarrantyStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "sales")
data class Sale(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "ticket_folio", nullable = false)
    val ticket: Ticket,

    @ManyToOne
    @JoinColumn(name = "part_id")
    val part: Part? = null,

    @Column(name = "part_name", length = 200)
    val partName: String? = null,

    val quantity: Byte = 1,

    @Column(nullable = false, precision = 10, scale = 2)
    val price: BigDecimal,

    @Column(name = "has_warranty")
    val hasWarranty: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "warranty_status")
    val warrantyStatus: WarrantyStatus = WarrantyStatus.PENDING,

    @Column(name = "warranty_expiration_date")
    val warrantyExpirationDate: LocalDate? = null
)