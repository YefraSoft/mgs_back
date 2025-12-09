package api.multipartes.dev.ticket.repository

import api.multipartes.dev.models.Ticket
import api.multipartes.dev.ticket.dto.PaymentMethodDto
import api.multipartes.dev.ticket.dto.SalesTrend
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface TicketRepository : JpaRepository<Ticket, String> {
    fun findByUserId(userId: Int): List<Ticket>
    fun findByDateBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<Ticket>

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

    @Query(
        "SELECT t.Payment_method as typeOfPayment, " +
                "COUNT(*) as numOfSales FROM tickets t " +
                "GROUP BY t.payment_method",
        nativeQuery = true
    )
    fun getPaymentStats(): List<PaymentMethodDto>
}