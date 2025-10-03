package api.multipartes.dev

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Service
class JwtService(@Value("\${jwt.secret}") private val jwtSecret: String) {

    fun generateToken(email: String): String {
        val key = SecretKeySpec(jwtSecret.toByteArray(), SignatureAlgorithm.HS256.jcaName)

        return Jwts.builder().setSubject(email).setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)).signWith(key).compact()
    }

    fun extractEmail(token: String): String? {
        val claims = Jwts.parserBuilder().setSigningKey(jwtSecret.toByteArray()).build().parseClaimsJws(token).body
        return claims.subject
    }
}