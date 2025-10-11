package api.multipartes.dev.repositories

import api.multipartes.dev.models.CustomerInvoiceData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerInvoiceDataRepository : JpaRepository<CustomerInvoiceData, String>
