package api.multipartes.dev.models

import api.multipartes.dev.enums.LogLevel
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "app_logs")
data class AppLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val level: LogLevel,

    @Column(nullable = false, columnDefinition = "TEXT")
    val message: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @ManyToOne
    @JoinColumn(name = "login_log_id")
    val loginLog: LoginLog? = null,

    @Column(length = 255)
    val context: String? = null,

    @Column(length = 255)
    val path: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
