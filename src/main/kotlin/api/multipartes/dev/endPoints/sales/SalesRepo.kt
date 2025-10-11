package api.multipartes.dev.endPoints.sales

import api.multipartes.dev.models.Sale
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface SalesRepo : JpaRepository<Sale, Int> {
    fun findByTicketFolio(ticketFolio: String): List<Sale>
    fun findByPartId(partId: Int): List<Sale>
}