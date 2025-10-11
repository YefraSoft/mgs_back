package api.multipartes.dev.config

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.TimeUnit

@Service
class RateLimitService {

    private val loginBuckets = Caffeine.newBuilder()
        .maximumSize(100_000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build<String, Bucket>()

    /**
     * Crea un bucket para login con límite de 5 intentos por minuto
     */
    private fun createLoginBucket(): Bucket {
        return Bucket.builder()
            .addLimit(
                Bandwidth.builder()
                    .capacity(5)
                    .refillIntervally(5, Duration.ofMinutes(1))
                    .build()
            )
            .build()
    }

    /**
     * Obtiene o crea un bucket para la IP especificada
     */
    private fun resolveBucketForLogin(key: String): Bucket {
        return loginBuckets.get(key) { createLoginBucket() }!!
    }

    /**
     * Verifica si la IP puede hacer un intento de login
     * @return true si se permite, false si se excedió el límite
     */
    fun allowLoginAttempt(ipAddress: String): Boolean {
        val bucket = resolveBucketForLogin(ipAddress)
        return bucket.tryConsume(1)
    }

    /**
     * Obtiene el tiempo de espera en segundos antes del próximo intento
     */
    fun getSecondsUntilRefill(ipAddress: String): Long {
        val bucket = resolveBucketForLogin(ipAddress)
        val probe = bucket.tryConsumeAndReturnRemaining(0)
        return if (probe.remainingTokens == 0L) {
            probe.nanosToWaitForRefill / 1_000_000_000
        } else {
            0
        }
    }
}
