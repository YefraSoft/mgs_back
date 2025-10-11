package api.multipartes.dev.endPoints.auth

import api.multipartes.dev.JwtService
import api.multipartes.dev.dtos.LoginRequest
import api.multipartes.dev.dtos.LoginResponse
import api.multipartes.dev.dtos.RegisterRequest
import api.multipartes.dev.models.User
import api.multipartes.dev.models.UserRole
import api.multipartes.dev.role.RoleRepository
import api.multipartes.dev.user.UserRepo
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val repo: UserRepo,
    private val roleRepo: RoleRepository,
    private val encoder: BCryptPasswordEncoder,
    private val jwtService: JwtService
) {


    fun login(request: LoginRequest): LoginResponse {
        val user = repo.findByUsername(request.username) ?: throw IllegalArgumentException("Invalid credentials!")

        if (!encoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Invalid credentials!")
        }
        val token = jwtService.generateToken(
            username = user.username,
            role = user.role.role.name,
            userId = user.id
        )

        return LoginResponse(user.role.role.name, token)
    }

    fun register(request: RegisterRequest): UserRole {
        if (repo.existsByUsername(request.username)) {
            throw IllegalArgumentException("Username already registered!")
        }

        val role = roleRepo.findById(request.roleId)
            .orElseThrow { IllegalArgumentException("Invalid role!") }

        val newUser = User(
            name = request.name,
            username = request.username,
            password = encoder.encode(request.password),
            role = role
        )
        val roleUser =  repo.save(newUser).role

        return roleUser
    }

}