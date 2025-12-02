package api.multipartes.dev.models

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(
    name = "sales", indexes = [
        Index(name = "idx_ticket_folio", columnList = "ticket_folio"),
        Index(name = "idx_part_id", columnList = "part_id")
    ]
)
data class Sale(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_folio", nullable = false)
    val ticket: Ticket,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    val part: Part? = null,

    @Column(name = "part_name", length = 200)
    val partName: String? = null,

    val quantity: Byte = 1,

    @Column(nullable = false, precision = 10, scale = 2)
    val price: BigDecimal,

    @OneToOne(mappedBy = "sale", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val warranty: Warranty? = null
    
)