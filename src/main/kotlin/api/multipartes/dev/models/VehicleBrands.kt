package api.multipartes.dev.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(
    name = "brands", indexes = [
        Index(name = "idx_name", columnList = "name")
    ]
)
data class VehicleBrands(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "Brand name is required")
    @Size(max = 50)
    val name: String
)
