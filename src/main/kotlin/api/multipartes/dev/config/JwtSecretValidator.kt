package api.multipartes.dev.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import jakarta.annotation.PostConstruct

@Configuration
class JwtSecretValidator {

    @Value("\${jwt.secret:}")
    private lateinit var jwtSecret: String

    @PostConstruct
    fun validateJwtSecret() {
        if (jwtSecret.isBlank()) {
            throw IllegalStateException(
                "CRITICAL SECURITY ERROR: JWT Secret is not configured! " +
                "Please set the 'jwt.secret' property in your configuration or as environment variable JWT_SECRET"
            )
        }

        if (jwtSecret.length < 32) {
            throw IllegalStateException(
                "CRITICAL SECURITY ERROR: JWT Secret is too short (${jwtSecret.length} characters). " +
                "It must be at least 32 characters long for adequate security."
            )
        }

        if (jwtSecret == "default" || jwtSecret == "secret" || jwtSecret == "changeme") {
            throw IllegalStateException(
                "CRITICAL SECURITY ERROR: JWT Secret is using a common/default value. " +
                "Please use a strong, randomly generated secret."
            )
        }
    }
}
