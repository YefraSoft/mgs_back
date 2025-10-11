package api.multipartes.dev.models

import api.multipartes.dev.enums.PaymentMethod
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "tickets")
data class Ticket(
    @Id
    @Column(columnDefinition = "CHAR(36)")
    val folio: String = java.util.UUID.randomUUID().toString(),

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false, precision = 10, scale = 2)
    val total: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    val paymentMethod: PaymentMethod,

    val items: Byte = 1,

    @Column(nullable = false)
    val date: LocalDateTime = LocalDateTime.now()
)
