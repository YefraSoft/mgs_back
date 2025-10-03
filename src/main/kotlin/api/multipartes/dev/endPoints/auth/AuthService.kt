package api.multipartes.dev.endPoints.auth

import api.multipartes.dev.JwtService
import api.multipartes.dev.dtos.LoginRequest
import api.multipartes.dev.dtos.LoginResponse
import api.multipartes.dev.dtos.RegisterRequest
import api.multipartes.dev.models.User
import api.multipartes.dev.role.RoleRepo
import api.multipartes.dev.user.UserRepo
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val repo: UserRepo,
    private val roleRepo: RoleRepo,
    private val encoder: BCryptPasswordEncoder,
    private val jwtService: JwtService
) {
    fun login(request: LoginRequest): LoginResponse {
        val user = repo.findByEmail(request.email) ?: throw IllegalArgumentException("User not found!")

        if (!encoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Wrong password!")
        }
        val token = jwtService.generateToken(user.email)
        return LoginResponse(user.role.id.toString(), token)
    }

    fun register(request: RegisterRequest): User {
        if (repo.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email Ready Exists!")
        }

        val role = roleRepo.findById(request.roleId)
            .orElseThrow { IllegalArgumentException("Wrong Rol!") }

        val newUser = User(
            name = request.name,
            email = request.email,
            password = encoder.encode(request.password),
            role = role
        )

        return repo.save(newUser)
    }

}