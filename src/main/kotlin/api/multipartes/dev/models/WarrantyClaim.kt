package api.multipartes.dev.models

import api.multipartes.dev.enums.ClaimType
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Entity
@Table(
    name = "warranty_claim", indexes = [
        Index(name = "idx_warranty_id", columnList = "warranty_id")
    ]
)
data class WarrantyClaim(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "warranty_id", nullable = false)
    val warranty: Warranty,

    @Column(name = "image_url", nullable = false, length = 500)
    @NotBlank(message = "Image URL is required")
    val imageUrl: String,

    @Column(length = 255)
    val description: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type", nullable = false)
    val claimType: ClaimType
)
