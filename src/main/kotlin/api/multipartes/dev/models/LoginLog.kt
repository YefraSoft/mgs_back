package api.multipartes.dev.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "login_logs", indexes = [
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_created_at", columnList = "created_at")
    ]
)
data class LoginLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "ip_address", length = 45)
    val ipAddress: String? = null,

    @Column(name = "user_agent", columnDefinition = "TEXT")
    val userAgent: String? = null,

    @Column(name = "role_snapshot", length = 50)
    val roleSnapshot: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
