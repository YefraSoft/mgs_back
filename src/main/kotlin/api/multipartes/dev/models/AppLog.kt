package api.multipartes.dev.models

import api.multipartes.dev.enums.LogLevel
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(
    name = "app_logs", indexes = [
        Index(name = "idx_level", columnList = "level"),
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_created_at", columnList = "created_at")
    ]
)
data class AppLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    val level: LogLevel,

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Message is required")
    val message: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login_log_id")
    val loginLog: LoginLog? = null,

    @Column(length = 255)
    val context: String? = null,

    @Column(length = 255)
    val path: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now()
)
