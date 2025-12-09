package api.multipartes.dev.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {
    @Value("\${cors.allowed.origin:http://localhost:3000}")
    private lateinit var allowedOrigin: String

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        
        // Permitir todos los orígenes temporalmente
        // Usar allowedOriginPatterns en lugar de allowedOrigins para permitir * con credentials
        configuration.allowedOriginPatterns = listOf("*")
        
        // Alternativa: Si prefieres usar la variable de entorno cuando esté definida:
        // val origins = if (allowedOrigin.isNotBlank() && allowedOrigin != "http://localhost:3000") {
        //     listOf(allowedOrigin)
        // } else {
        //     listOf("*")
        // }
        // configuration.allowedOriginPatterns = origins

        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")

        configuration.allowedHeaders = listOf("Authorization", "Content-Type", "Accept", "X-Requested-With")

        configuration.exposedHeaders = listOf("Authorization")

        configuration.allowCredentials = true

        configuration.maxAge = 3600L
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}