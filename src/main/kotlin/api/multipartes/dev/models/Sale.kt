package api.multipartes.dev.models

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "sales")
data class Sale(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "ticket_folio", nullable = false)
    val ticket: Ticket,

    @ManyToOne
    @JoinColumn(name = "part_id", nullable = false)
    val part: Part,

    val quantity: Byte = 1,

    @Column(nullable = false, precision = 10, scale = 2)
    val price: BigDecimal
)