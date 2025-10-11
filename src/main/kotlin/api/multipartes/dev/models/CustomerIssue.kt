package api.multipartes.dev.models

import api.multipartes.dev.enums.IssueStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "customer_issues")
data class CustomerIssue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false)
    val problem: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: IssueStatus = IssueStatus.PENDING,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customer: Customer? = null
)
