package api.multipartes.dev.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(
    name = "users", indexes = [
        Index(name = "idx_username", columnList = "username"),
        Index(name = "idx_role_id", columnList = "role_id")
    ]
)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Name is required")
    val name: String,

    @ManyToOne
    @JoinColumn(name = "role_id")
    val role: UserRole? = null,

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "Username is required")
    val username: String,

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    val password: String
)
