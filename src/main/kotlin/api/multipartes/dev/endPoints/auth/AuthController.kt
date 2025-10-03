package api.multipartes.dev.endPoints.auth

import api.multipartes.dev.dtos.LoginRequest
import api.multipartes.dev.dtos.LoginResponse
import api.multipartes.dev.dtos.RegisterRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val service: AuthService) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(service.login(request))
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<Any> {
        return try {
            val newUser = service.register(request)
            ResponseEntity.ok(newUser)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(409).body(mapOf("error" to e.message))
        }
    }
}