package api.multipartes.dev.models

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false, length = 50)
    val name: String,

    @Column(unique = true, nullable = false, length = 50)
    val username: String,

    @Column(nullable = false)
    val password: String,

    @ManyToOne
    @JoinColumn(name = "role_id")
    val role: UserRole
)
