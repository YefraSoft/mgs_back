package api.multipartes.dev.models

import api.multipartes.dev.enums.TransmissionType
import jakarta.persistence.*


@Entity
@Table(name = "models")
data class Model(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    val brand: VehicleBrands,

    @Column(name = "serial_number")
    val serialNumber: String? = null,

    @Column(nullable = false)
    val name: String,

    val year: Int? = null,

    @Enumerated(EnumType.STRING)
    val transmission: TransmissionType? = null,

    @Column(nullable = false)
    val engine: String,

    @Column(name = "vehicle_class", nullable = false)
    val vehicleClass: String
)
