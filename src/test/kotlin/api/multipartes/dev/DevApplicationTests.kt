package api.multipartes.dev

import api.multipartes.dev.endPoints.brands.BrandRepository
import api.multipartes.dev.endPoints.brands.BrandService
import api.multipartes.dev.endPoints.customers.CustomerRepository
import api.multipartes.dev.endPoints.customers.CustomerIssueRepository
import api.multipartes.dev.endPoints.models.ModelRepository
import api.multipartes.dev.endPoints.models.ModelService
import api.multipartes.dev.endPoints.parts.PartsRepo
import api.multipartes.dev.sales.repository.SalesRepository
import api.multipartes.dev.ticket.repository.TicketRepository
import api.multipartes.dev.config.JwtSecretValidator
import api.multipartes.dev.config.RateLimitService
import api.multipartes.dev.config.GlobalExceptionHandler
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@ActiveProfiles("dev")
class DevApplicationTests {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var brandRepository: BrandRepository

    @Autowired
    private lateinit var modelRepository: ModelRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var customerIssueRepository: CustomerIssueRepository

    @Autowired
    private lateinit var partsRepository: PartsRepo

    @Autowired
    private lateinit var salesRepository: SalesRepository

    @Autowired
    private lateinit var ticketRepository: TicketRepository

    @Autowired
    private lateinit var brandService: BrandService

    @Autowired
    private lateinit var modelService: ModelService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var jwtSecretValidator: JwtSecretValidator

    @Autowired
    private lateinit var rateLimitService: RateLimitService

    @Autowired
    private lateinit var globalExceptionHandler: GlobalExceptionHandler

    @Test
    fun contextLoads() {
        assertNotNull(applicationContext)
    }

    @Test
    fun testSpringBootContextInitialization() {
        assertTrue(applicationContext.containsBean("brandRepository"), "BrandRepository bean should exist")
        assertTrue(applicationContext.containsBean("modelRepository"), "ModelRepository bean should exist")
        assertTrue(applicationContext.containsBean("customerRepository"), "CustomerRepository bean should exist")
        assertTrue(applicationContext.containsBean("partsRepo"), "PartsRepo bean should exist")
    }

    @Test
    fun testJpaRepositoriesAreInjected() {
        assertNotNull(brandRepository, "BrandRepository should be injected")
        assertNotNull(modelRepository, "ModelRepository should be injected")
        assertNotNull(customerRepository, "CustomerRepository should be injected")
        assertNotNull(customerIssueRepository, "CustomerIssueRepository should be injected")
        assertNotNull(partsRepository, "PartsRepository should be injected")
        assertNotNull(salesRepository, "SalesRepository should be injected")
        assertNotNull(ticketRepository, "TicketRepository should be injected")
    }

    @Test
    fun testServiceBeansAreInjected() {
        assertNotNull(brandService, "BrandService should be injected")
        assertNotNull(modelService, "ModelService should be injected")
    }

    @Test
    fun testSecurityComponentsAreConfigured() {
        assertNotNull(passwordEncoder, "PasswordEncoder should be injected")
        assertNotNull(jwtSecretValidator, "JwtSecretValidator should be injected")
        assertNotNull(rateLimitService, "RateLimitService should be injected")
        assertNotNull(globalExceptionHandler, "GlobalExceptionHandler should be injected")
    }

    @Test
    fun testPasswordEncoderWorks() {
        val rawPassword = "testPassword123"
        val encodedPassword = passwordEncoder.encode(rawPassword)
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), "Password should match after encoding")
    }

    @Test
    fun testJpaRepositoriesExtendJpaRepository() {
        assertTrue(brandRepository is JpaRepository<*, *>, "BrandRepository should extend JpaRepository")
        assertTrue(modelRepository is JpaRepository<*, *>, "ModelRepository should extend JpaRepository")
        assertTrue(customerRepository is JpaRepository<*, *>, "CustomerRepository should extend JpaRepository")
        assertTrue(partsRepository is JpaRepository<*, *>, "PartsRepo should extend JpaRepository")
    }

    @Test
    fun testApplicationContextContainsAllRequiredBeans() {
        val requiredBeans = listOf(
            "brandRepository",
            "modelRepository",
            "customerRepository",
            "customerIssueRepository",
            "partsRepo",
            "salesRepo",
            "ticketRepository",
            "brandService",
            "modelService",
            "passwordEncoder",
            "jwtSecretValidator",
            "rateLimitService",
            "globalExceptionHandler"
        )

        requiredBeans.forEach { beanName ->
            assertTrue(
                applicationContext.containsBean(beanName),
                "ApplicationContext should contain bean: $beanName"
            )
        }
    }

    @Test
    fun testDatabaseConnectionIsEstablished() {
        // Verifica que al menos se pueda acceder a los repositorios
        // si la conexión no estuviera establecida, esto fallaría
        assertNotNull(brandRepository, "Should be able to access BrandRepository after DB connection")
        assertNotNull(modelRepository, "Should be able to access ModelRepository after DB connection")
    }

    @Test
    fun testApplicationProfileIsDevProfile() {
        val activeProfiles = applicationContext.environment.activeProfiles
        assertTrue(
            activeProfiles.contains("dev"),
            "Application should be running with 'dev' profile"
        )
    }

}
