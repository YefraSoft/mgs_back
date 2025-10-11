package api.multipartes.dev.models

import api.multipartes.dev.enums.SideType
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "parts")
data class Part(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    val code: String? = null,

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val side: SideType,

    @ManyToOne
    @JoinColumn(name = "part_category_id", nullable = false)
    val partCategory: PartCategory,

    val color: String? = null,

    @Column(nullable = false, precision = 10, scale = 2)
    val price: BigDecimal,

    val quantity: Byte = 1
)