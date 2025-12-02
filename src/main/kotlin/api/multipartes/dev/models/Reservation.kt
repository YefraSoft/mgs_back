package api.multipartes.dev.models

import api.multipartes.dev.enums.ReservationStatus
import jakarta.persistence.*

@Entity
@Table(
    name = "reservations", indexes = [
        Index(name = "idx_customer_id", columnList = "customer_id"),
        Index(name = "idx_part_id", columnList = "part_id"),
        Index(name = "idx_status", columnList = "status"),
        Index(name = "idx_expiration", columnList = "expiration_date")
    ]
)
data class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    val customer: Customer,

    @Column(nullable = false, precision = 10, scale = 2)
    val deposit: java.math.BigDecimal,

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    val totalPrice: java.math.BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    val part: Part? = null,

    @Column(name = "part_name")
    val partName: String? = null,

    @Column(nullable = false, precision = 10, scale = 2)
    val balance: java.math.BigDecimal,

    @Column(name = "expiration_date", nullable = false)
    val expirationDate: java.time.LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    val status: ReservationStatus = ReservationStatus.ACTIVE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_ticket_folio")
    val completedTicket: Ticket? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @Column(name = "completed_at")
    val completedAt: java.time.LocalDateTime? = null,

    @OneToMany(mappedBy = "reservation", cascade = [CascadeType.ALL])
    val partsReserved: List<PartReserved> = listOf()
)
