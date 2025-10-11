package api.multipartes.dev

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Service
class JwtService(
    @Value("\${jwt.secret}") private val jwtSecret: String,
    @Value("\${jwt.expiration:86400000}") private val jwtExpiration: Long
) {

    /*
    * BUILDER JWT
    */

    fun generateToken(username: String, role: String, userId: Int?): String {
        val claims = mutableMapOf<String, String>()
        claims["username"] = username
        claims["role"] = role
        userId?.let { claims["userId"] = it.toString() }

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(getSigningKey())
            .compact()
    }

    /*
    * PRIVATE MEMBERS
    */

    private fun extractClaims(token: String): Claims? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(jwtSecret.toByteArray())
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            null
        }
    }


    private fun getSigningKey(): SecretKeySpec {
        return SecretKeySpec(jwtSecret.toByteArray(), SignatureAlgorithm.HS256.jcaName)
    }

    /*
    * EXTRACTIONS OF TOKEN
    */

    fun extractUsername(token: String): String? {
        return extractClaims(token)?.subject
    }

    fun extractRole(token: String): String? {
        return extractClaims(token)?.get("role", String::class.java)
    }

    fun extractUserId(token: String): String? {
        return extractClaims(token)?.get("userId", String::class.java)
    }

    fun extractExpiration(token: String): Date? {
        return extractClaims(token)?.expiration
    }

    /*
    * VALIDATIONS OF TOKENS
    */

    fun isTokenValid(token: String): Boolean {
        return try {
            val claims = extractClaims(token)
            claims != null && claims.expiration.after(Date())
        } catch (e: Exception) {
            false
        }
    }
}