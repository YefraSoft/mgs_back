package api.multipartes.dev.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "parts_images")
data class PartImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "part_id", nullable = false)
    val part: Part,

    @Column(name = "image_url", nullable = false, length = 500)
    val imageUrl: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
