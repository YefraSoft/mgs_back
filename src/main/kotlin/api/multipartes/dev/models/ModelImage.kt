package api.multipartes.dev.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "model_images")
data class ModelImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    val model: Model,

    @Column(name = "image_url", nullable = false, length = 500)
    val imageUrl: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
