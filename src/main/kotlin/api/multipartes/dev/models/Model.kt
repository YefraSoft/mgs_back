package api.multipartes.dev.models

import api.multipartes.dev.enums.TransmissionType
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank


@Entity
@Table(
    name = "models", indexes = [
        Index(name = "idx_brand_id", columnList = "brand_id"),
        Index(name = "idx_year", columnList = "year")
    ]
)
data class Model(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    val brand: VehicleBrands,

    @Column(name = "serial_number")
    val serialNumber: String? = null,

    @Column(nullable = false)
    @NotBlank(message = "Model name is required")
    val name: String,

    val year: Int? = null,

    @Enumerated(EnumType.STRING)
    val transmission: TransmissionType? = null,

    @Column(nullable = false)
    @NotBlank(message = "Engine is required")
    val engine: String,

    @Column(name = "vehicle_class", nullable = false)
    @NotBlank(message = "Vehicle class is required")
    val vehicleClass: String
)
