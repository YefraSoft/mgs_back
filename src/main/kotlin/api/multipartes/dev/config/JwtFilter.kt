package api.multipartes.dev.config

import api.multipartes.dev.JwtService
import api.multipartes.dev.user.repository.UserRepository
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
class JwtFilter(private val jwtService: JwtService, private val userRepo: UserRepository) :
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
                if (!jwtService.isTokenValid(token)) {
                    sendCustomError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token")
                    return
                }
                val username = jwtService.extractUsername(token)
                if (username != null && SecurityContextHolder.getContext().authentication == null) {
                    var role = jwtService.extractRole(token)
                    if (role.isNullOrBlank()) {
                        val user = userRepo.findByUsername(username)
                            ?: run {
                                sendCustomError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid authentication")
                                return
                            }
                        role = user.role?.role?.name
                            ?: run {
                                sendCustomError(response, HttpServletResponse.SC_UNAUTHORIZED, "User without role")
                                return
                            }
                    }
                    val authorities = listOf(SimpleGrantedAuthority("ROLE_${role.uppercase()}"))
                    val authentication = UsernamePasswordAuthenticationToken(
                        username, null, authorities
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (e: SignatureException) {
                sendCustomError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token signature")
                return
            } catch (e: ExpiredJwtException) {
                sendCustomError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token has expired")
                return
            } catch (e: Exception) {
                sendCustomError(response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed")
                return
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun sendCustomError(response: HttpServletResponse, status: Int, message: String) {
        response.status = status
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write("{\"error\": \"$message\"}")
        response.writer.flush()
    }

}