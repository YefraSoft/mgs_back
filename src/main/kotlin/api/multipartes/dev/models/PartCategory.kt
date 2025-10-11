package api.multipartes.dev.models

import api.multipartes.dev.enums.CategoryType
import jakarta.persistence.*

@Entity
@Table(name = "part_categories")
data class PartCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val category: CategoryType
)
