package api.multipartes.dev.models

import api.multipartes.dev.enums.RoleType
import jakarta.persistence.*

@Entity
@Table(name = "roles")
data class UserRole(
    @Id
    val id: Byte,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: RoleType
)
