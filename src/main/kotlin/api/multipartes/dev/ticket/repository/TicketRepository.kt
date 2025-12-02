package api.multipartes.dev.ticket.repository

import api.multipartes.dev.models.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface TicketRepository : JpaRepository<Ticket, String> {
    fun findByUserId(userId: Int): List<Ticket>
    fun findByDateBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<Ticket>
}