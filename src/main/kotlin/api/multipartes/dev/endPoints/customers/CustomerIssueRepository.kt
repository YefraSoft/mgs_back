package api.multipartes.dev.endPoints.customers

import api.multipartes.dev.enums.IssueStatus
import api.multipartes.dev.models.CustomerIssue
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerIssueRepository : JpaRepository<CustomerIssue, Int> {
    fun findByStatus(status: IssueStatus): List<CustomerIssue>
    fun findByCustomerId(customerId: Int): List<CustomerIssue>
    fun findByStatusAndCustomerId(status: IssueStatus, customerId: Int): List<CustomerIssue>
}
