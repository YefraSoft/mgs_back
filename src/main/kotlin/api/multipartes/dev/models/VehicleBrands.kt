package api.multipartes.dev.models

import jakarta.persistence.*

@Entity
@Table(name = "brands")
data class VehicleBrands(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false, length = 50)
    val name: String
)
