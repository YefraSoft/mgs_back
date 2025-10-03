package api.multipartes.dev.role

import api.multipartes.dev.models.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepo : JpaRepository<Role, Byte> {
}