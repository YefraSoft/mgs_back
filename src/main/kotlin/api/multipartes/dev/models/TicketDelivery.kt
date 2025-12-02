package api.multipartes.dev.models

import api.multipartes.dev.enums.DeliveryStatus
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(
    name = "ticket_delivery", indexes = [
        Index(name = "idx_ticket_folio_delivery", columnList = "ticket_folio"),
        Index(name = "idx_customer_id_delivery", columnList = "customer_id"),
        Index(name = "idx_delivery_status", columnList = "delivery_status")
    ]
)
data class TicketDelivery(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_folio", nullable = false)
    val ticket: Ticket,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    val customer: Customer,

    @Column(name = "delivery_address", nullable = false)
    @NotBlank(message = "Delivery address is required")
    val deliveryAddress: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false, length = 15)
    val deliveryStatus: DeliveryStatus = DeliveryStatus.PENDING,

    @Column(name = "delivered_at")
    val deliveredAt: java.time.LocalDateTime? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now()
)
