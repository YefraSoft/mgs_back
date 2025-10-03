package api.multipartes.dev.models

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "parts")
data class Part(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    val stock: Byte? = null,
    val name: String,
    val feature: String? = null,
    val state: String? = null,
    val side: String? = null,
    val position: Byte? = null,
    val price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "model_id")
    val model: Model? = null,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)