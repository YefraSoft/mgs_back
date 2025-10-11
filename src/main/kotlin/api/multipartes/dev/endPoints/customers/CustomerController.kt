package api.multipartes.dev.endPoints.customers

import api.multipartes.dev.dtos.CreateCustomerRequest
import api.multipartes.dev.dtos.CustomerResponse
import api.multipartes.dev.dtos.UpdateCustomerRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
) {

    @GetMapping
    fun getAllCustomers(): ResponseEntity<List<CustomerResponse>> {
        return ResponseEntity.ok(customerService.getAllCustomers())
    }

    @GetMapping("/{id}")
    fun getCustomerById(@PathVariable id: Int): ResponseEntity<CustomerResponse> {
        return try {
            ResponseEntity.ok(customerService.getCustomerById(id))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/search/by-phone/{phone}")
    fun getCustomerByPhone(@PathVariable phone: String): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(customerService.getCustomerByPhone(phone))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/search/by-rfc/{rfc}")
    fun getCustomerByRfc(@PathVariable rfc: String): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(customerService.getCustomerByRfc(rfc))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/search/by-name")
    fun searchCustomersByName(@RequestParam name: String): ResponseEntity<List<CustomerResponse>> {
        return ResponseEntity.ok(customerService.searchCustomersByName(name))
    }

    @PostMapping
    fun createCustomer(@RequestBody request: CreateCustomerRequest): ResponseEntity<Any> {
        return try {
            val customer = customerService.createCustomer(request)
            ResponseEntity.status(HttpStatus.CREATED).body(customer)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}")
    fun updateCustomer(
        @PathVariable id: Int,
        @RequestBody request: UpdateCustomerRequest
    ): ResponseEntity<Any> {
        return try {
            val customer = customerService.updateCustomer(id, request)
            ResponseEntity.ok(customer)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable id: Int): ResponseEntity<Any> {
        return try {
            customerService.deleteCustomer(id)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}
