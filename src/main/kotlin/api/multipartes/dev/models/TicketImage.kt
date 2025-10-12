package api.multipartes.dev.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "ticket_images")
data class TicketImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "ticket_folio", nullable = false)
    val ticket: Ticket,

    @Column(name = "image_url", nullable = false, length = 500)
    val imageUrl: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
