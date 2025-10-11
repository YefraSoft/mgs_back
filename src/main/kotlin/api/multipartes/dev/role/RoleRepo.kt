package api.multipartes.dev.role

import api.multipartes.dev.models.UserRole
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<UserRole, Byte> {
}