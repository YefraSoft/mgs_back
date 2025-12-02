package api.multipartes.dev.models

import jakarta.persistence.*

@Entity
@Table(
    name = "parts_reserved", indexes = [
        Index(name = "idx_reservation_id", columnList = "reservation_id"),
        Index(name = "idx_part_id", columnList = "part_id")
    ]
)
data class PartReserved(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    val reservation: Reservation,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    val part: Part? = null,

    @Column(name = "part_name")
    val partName: String? = null,

    @Column(nullable = false)
    val quantity: Byte = 1,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now()
)
