package api.multipartes.dev.endPoints.sales

import api.multipartes.dev.models.Sale
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface SalesRepo : JpaRepository<Sale, Int> {
    fun findByPartId(partId: Int): List<Sale> // Buscar ventas por parte
    fun findByCreatedAtBetween(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Sale> // Buscar ventas entre fechas
}