package api.multipartes.dev.models

import api.multipartes.dev.enums.CategoryType
import api.multipartes.dev.enums.SideType
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(
    name = "parts", indexes = [
        Index(name = "idx_model_id", columnList = "model_id"),
        Index(name = "idx_code", columnList = "code")
    ]
)
data class Part(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    val code: String? = null,

    @Column(nullable = false)
    @NotBlank(message = "Part name is required")
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val side: SideType,

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    val categoryType: CategoryType,

    val color: String? = null,

    @Column(nullable = false, precision = 10, scale = 2)
    val price: BigDecimal,

    val quantity: Byte = 1,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    val model: Model? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)