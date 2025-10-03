package api.multipartes.dev.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "login_logs")
data class LoginLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "role_user")
    val roleUser: Role,

    @Column(name = "ip_address")
    val ipAddress: String,

    @Column(name = "user_agent", columnDefinition = "TEXT")
    val userAgent: String,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
