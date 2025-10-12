package api.multipartes.dev.models

import api.multipartes.dev.enums.ClaimType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "warranty_claim")
data class WarrantyClaim(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "sale_id", nullable = false)
    val sale: Sale,

    @Column(name = "image_url", nullable = false, length = 500)
    val imageUrl: String,

    @Column(length = 255)
    val description: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type", nullable = false)
    val claimType: ClaimType
)
