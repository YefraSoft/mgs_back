package api.multipartes.dev.endPoints.customers

import api.multipartes.dev.models.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : JpaRepository<Customer, Int> {
    fun findByPhone(phone: String): Customer?
    fun findByInvoiceData_Rfc(rfc: String): Customer?
    fun findByNameContainingIgnoreCase(name: String): List<Customer>
    fun existsByPhone(phone: String): Boolean
}
