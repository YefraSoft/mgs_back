package api.multipartes.dev.models

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "sales")
data class Sale(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "part_id")
    val part: Part? = null,

    @Column(name = "custom_part_name")
    val customPartName: String? = null,

    val quantity: Byte = 1,
    val price: BigDecimal,
    @Column(name = "payment_method")
    val paymentMethod: String,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)