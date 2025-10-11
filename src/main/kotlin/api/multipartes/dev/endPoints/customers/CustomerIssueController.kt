package api.multipartes.dev.endPoints.customers

import api.multipartes.dev.dtos.CreateCustomerIssueRequest
import api.multipartes.dev.dtos.CustomerIssueResponse
import api.multipartes.dev.dtos.UpdateCustomerIssueRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customer-issues")
class CustomerIssueController(
    private val issueService: CustomerIssueService
) {

    @GetMapping
    fun getAllIssues(): ResponseEntity<List<CustomerIssueResponse>> {
        return ResponseEntity.ok(issueService.getAllIssues())
    }

    @GetMapping("/{id}")
    fun getIssueById(@PathVariable id: Int): ResponseEntity<CustomerIssueResponse> {
        return try {
            ResponseEntity.ok(issueService.getIssueById(id))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/search/by-status/{status}")
    fun getIssuesByStatus(@PathVariable status: String): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(issueService.getIssuesByStatus(status))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/search/by-customer/{customerId}")
    fun getIssuesByCustomer(@PathVariable customerId: Int): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(issueService.getIssuesByCustomer(customerId))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/search/by-status-and-customer")
    fun getIssuesByStatusAndCustomer(
        @RequestParam status: String,
        @RequestParam customerId: Int
    ): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(issueService.getIssuesByStatusAndCustomer(status, customerId))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PostMapping
    fun createIssue(@RequestBody request: CreateCustomerIssueRequest): ResponseEntity<Any> {
        return try {
            val issue = issueService.createIssue(request)
            ResponseEntity.status(HttpStatus.CREATED).body(issue)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}")
    fun updateIssue(
        @PathVariable id: Int,
        @RequestBody request: UpdateCustomerIssueRequest
    ): ResponseEntity<Any> {
        return try {
            val issue = issueService.updateIssue(id, request)
            ResponseEntity.ok(issue)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @DeleteMapping("/{id}")
    fun deleteIssue(@PathVariable id: Int): ResponseEntity<Any> {
        return try {
            issueService.deleteIssue(id)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}
