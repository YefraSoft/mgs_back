package api.multipartes.dev.endPoints.customers

import api.multipartes.dev.dtos.CreateCustomerIssueRequest
import api.multipartes.dev.dtos.CustomerIssueResponse
import api.multipartes.dev.dtos.UpdateCustomerIssueRequest
import api.multipartes.dev.enums.IssueStatus
import api.multipartes.dev.models.CustomerIssue
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CustomerIssueService(
    private val issueRepository: CustomerIssueRepository,
    private val customerRepository: CustomerRepository
) {

    @Cacheable("customer-issues")
    fun getAllIssues(): List<CustomerIssueResponse> {
        return issueRepository.findAll().map { issue ->
            mapToResponse(issue)
        }
    }

    fun getIssueById(id: Int): CustomerIssueResponse {
        val issue = issueRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Customer issue with ID $id not found") }
        return mapToResponse(issue)
    }

    fun getIssuesByStatus(status: String): List<CustomerIssueResponse> {
        val issueStatus = try {
            IssueStatus.valueOf(status.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid status. Must be ATTENDED, REJECTED, NOT_FOUND, or PENDING")
        }

        return issueRepository.findByStatus(issueStatus).map { issue ->
            mapToResponse(issue)
        }
    }

    fun getIssuesByCustomer(customerId: Int): List<CustomerIssueResponse> {
        if (!customerRepository.existsById(customerId)) {
            throw IllegalArgumentException("Customer with ID $customerId not found")
        }

        return issueRepository.findByCustomerId(customerId).map { issue ->
            mapToResponse(issue)
        }
    }

    fun getIssuesByStatusAndCustomer(status: String, customerId: Int): List<CustomerIssueResponse> {
        if (!customerRepository.existsById(customerId)) {
            throw IllegalArgumentException("Customer with ID $customerId not found")
        }

        val issueStatus = try {
            IssueStatus.valueOf(status.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid status. Must be ATTENDED, REJECTED, NOT_FOUND, or PENDING")
        }

        return issueRepository.findByStatusAndCustomerId(issueStatus, customerId).map { issue ->
            mapToResponse(issue)
        }
    }

    @CacheEvict(value = ["customer-issues"], allEntries = true)
    fun createIssue(request: CreateCustomerIssueRequest): CustomerIssueResponse {
        var customer: api.multipartes.dev.models.Customer? = null
        if (request.customerId != null) {
            customer = customerRepository.findById(request.customerId).orElse(null)
            if (customer == null) {
                throw IllegalArgumentException("Customer with ID ${request.customerId} not found")
            }
        }

        val newIssue = CustomerIssue(
            problem = request.problem,
            status = IssueStatus.PENDING,
            customer = customer
        )

        val savedIssue = issueRepository.save(newIssue)
        return mapToResponse(savedIssue)
    }

    @CacheEvict(value = ["customer-issues"], allEntries = true)
    fun updateIssue(id: Int, request: UpdateCustomerIssueRequest): CustomerIssueResponse {
        val existingIssue = issueRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Customer issue with ID $id not found") }

        val status = try {
            IssueStatus.valueOf(request.status.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid status. Must be ATTENDED, REJECTED, NOT_FOUND, or PENDING")
        }

        var customer: api.multipartes.dev.models.Customer? = null
        if (request.customerId != null) {
            customer = customerRepository.findById(request.customerId).orElse(null)
            if (customer == null) {
                throw IllegalArgumentException("Customer with ID ${request.customerId} not found")
            }
        }

        val updatedIssue = existingIssue.copy(
            problem = request.problem,
            status = status,
            customer = customer
        )

        val savedIssue = issueRepository.save(updatedIssue)
        return mapToResponse(savedIssue)
    }

    @CacheEvict(value = ["customer-issues"], allEntries = true)
    fun deleteIssue(id: Int) {
        if (!issueRepository.existsById(id)) {
            throw IllegalArgumentException("Customer issue with ID $id not found")
        }
        issueRepository.deleteById(id)
    }

    private fun mapToResponse(issue: CustomerIssue): CustomerIssueResponse {
        return CustomerIssueResponse(
            id = issue.id!!,
            problem = issue.problem,
            status = issue.status.name,
            createdAt = issue.createdAt,
            customerId = issue.customer?.id,
            customerName = issue.customer?.name
        )
    }
}
