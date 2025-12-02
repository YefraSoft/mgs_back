package api.multipartes.dev.sales.repository

import api.multipartes.dev.models.Sale
import org.springframework.data.jpa.repository.JpaRepository

interface SalesRepository : JpaRepository<Sale, Int> {
    fun findByTicketFolio(ticketFolio: String): List<Sale>
    fun findByPartId(partId: Int): List<Sale>
}