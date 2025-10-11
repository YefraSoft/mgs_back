package api.multipartes.dev.user

import api.multipartes.dev.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepo : JpaRepository<User, Int> {
    fun findByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean
}