package api.multipartes.dev.models

import api.multipartes.dev.enums.WarrantyStatus
import jakarta.persistence.*

@Entity
@Table(
    name = "warranty", indexes = [
        Index(name = "idx_sale_id", columnList = "sale_id"),
        Index(name = "idx_status", columnList = "status")
    ]
)
data class Warranty(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    val sale: Sale,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    val status: WarrantyStatus = WarrantyStatus.PENDING,

    @Column(name = "expiration_date")
    val expirationDate: java.time.LocalDate? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @OneToMany(mappedBy = "warranty", cascade = [CascadeType.ALL])
    val claims: List<WarrantyClaim> = listOf()
)
