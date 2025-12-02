package api.multipartes.dev.models

import api.multipartes.dev.enums.ExpenseCategory
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(
    name = "expenses", indexes = [
        Index(name = "idx_expense_date", columnList = "pay_before"),
        Index(name = "idx_category", columnList = "category"),
        Index(name = "idx_created_by", columnList = "created_by")
    ]
)
data class Expense(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false)
    @NotBlank(message = "Expense name is required")
    val name: String,

    @Column(nullable = false, precision = 10, scale = 2)
    val amount: java.math.BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val category: ExpenseCategory,

    @Column(name = "pay_before", nullable = false)
    val payBefore: java.time.LocalDate,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @Column(name = "payment_at")
    val paymentAt: java.time.LocalDate? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    val createdBy: User? = null
)