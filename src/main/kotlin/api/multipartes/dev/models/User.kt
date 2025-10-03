package api.multipartes.dev.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    val name: String,

    @Column(unique = true, nullable = false)
    val email: String,

    val password: String,

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    val role: Role,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
