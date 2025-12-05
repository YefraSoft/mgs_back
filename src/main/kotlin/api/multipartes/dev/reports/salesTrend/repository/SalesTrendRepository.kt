package api.multipartes.dev.reports.salesTrend.repository

import api.multipartes.dev.models.Ticket
import api.multipartes.dev.reports.salesTrend.dto.SalesTrend
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SalesTrendRepository : JpaRepository<Ticket, String> {

    @Query(
        value = """
            SELECT 
                DATE_FORMAT(t.date, '%Y-%m') AS periodo,
                SUM(t.total) AS totalVendido,
                COUNT(*) AS cantidadDeVentas
            FROM tickets t
            GROUP BY DATE_FORMAT(t.date, '%Y-%m')
            ORDER BY periodo
        """,
        nativeQuery = true
    )
    fun findMonthlySales(): List<SalesTrend>?
}