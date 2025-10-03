package api.multipartes.dev.config

import api.multipartes.dev.JwtService
import api.multipartes.dev.user.UserRepo
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(private val jwtService: JwtService, private val userRepo: UserRepo) :
    OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(7)
            try {
                val email = jwtService.extractEmail(token)
                if (email != null && SecurityContextHolder.getContext().authentication == null) {
                    val user = userRepo.findByEmail(email)
                    if (user == null) {
                        sendCustomError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not found!")
                        return
                    }
                    val role = user.role.role ?: "user"
                    val authorities = listOf(SimpleGrantedAuthority("ROLE_${role.toString().uppercase()}"))
                    val authentication = UsernamePasswordAuthenticationToken(
                        email, null, authorities
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (e: SignatureException) {
                sendCustomError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token Signature")
                return
            } catch (e: ExpiredJwtException) {
                sendCustomError(response, HttpServletResponse.SC_UNAUTHORIZED, "Expired Token")
                return
            } catch (e: Exception) {
                sendCustomError(response, HttpServletResponse.SC_FORBIDDEN, "Invalid Token or Access Denied")
                return
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun sendCustomError(response: HttpServletResponse, status: Int, message: String) {
        response.status = status
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(message)
        response.writer.flush()
    }

    override fun destroy() {}
}