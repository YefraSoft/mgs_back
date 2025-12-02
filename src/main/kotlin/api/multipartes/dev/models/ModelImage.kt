package api.multipartes.dev.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Entity
@Table(
    name = "model_images", indexes = [
        Index(name = "idx_model_id", columnList = "model_id")
    ]
)
data class ModelImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    val model: Model,

    @Column(name = "image_url", nullable = false, length = 500)
    @NotBlank(message = "Image URL is required")
    val imageUrl: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
