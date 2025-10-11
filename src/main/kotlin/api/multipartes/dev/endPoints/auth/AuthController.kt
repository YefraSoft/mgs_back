package api.multipartes.dev.endPoints.auth

import api.multipartes.dev.config.RateLimitService
import api.multipartes.dev.dtos.LoginRequest
import api.multipartes.dev.dtos.LoginResponse
import api.multipartes.dev.dtos.RegisterRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val service: AuthService,
    private val rateLimitService: RateLimitService
) {

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
        httpRequest: HttpServletRequest
    ): ResponseEntity<Any> {
        val ipAddress = getClientIp(httpRequest)
        
        if (!rateLimitService.allowLoginAttempt(ipAddress)) {
            val waitSeconds = rateLimitService.getSecondsUntilRefill(ipAddress)
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
                mapOf(
                    "error" to "Too many login attempts",
                    "message" to "You have exceeded the maximum number of login attempts. Please try again in $waitSeconds seconds.",
                    "retryAfter" to waitSeconds
                )
            )
        }

        return try {
            ResponseEntity.ok(service.login(request))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                mapOf("error" to e.message)
            )
        }
    }

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<Any> {
        return try {
            val newUser = service.register(request)
            ResponseEntity.ok(newUser)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(409).body(mapOf("error" to e.message))
        }
    }

    /**
     * Obtiene la IP real del cliente considerando proxies y load balancers
     */
    private fun getClientIp(request: HttpServletRequest): String {
        val xForwardedFor = request.getHeader("X-Forwarded-For")
        if (!xForwardedFor.isNullOrBlank()) {
            return xForwardedFor.split(",")[0].trim()
        }

        val xRealIp = request.getHeader("X-Real-IP")
        if (!xRealIp.isNullOrBlank()) {
            return xRealIp.trim()
        }

        return request.remoteAddr ?: "unknown"
    }
}