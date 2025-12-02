package api.multipartes.dev.models

import jakarta.persistence.*

@Entity
@Table(
    name = "purchase_vehicles", indexes = [
        Index(name = "idx_model_id", columnList = "model_id"),
        Index(name = "idx_purchase_date", columnList = "purchase_date")
    ]
)
data class PurchaseVehicle(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    val model: Model,

    @Column(name = "purchase_date")
    val purchaseDate: java.time.LocalDate? = null,

    @Column(name = "purchase_cost", precision = 10, scale = 2)
    val purchaseCost: java.math.BigDecimal? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now()
)
