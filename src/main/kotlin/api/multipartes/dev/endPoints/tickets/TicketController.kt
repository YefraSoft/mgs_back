package api.multipartes.dev.endPoints.tickets

import api.multipartes.dev.dtos.CreateTicketRequest
import api.multipartes.dev.dtos.TicketResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/tickets")
class TicketController(private val ticketService: TicketService) {

    @PostMapping
    fun createTicket(@RequestBody request: CreateTicketRequest): ResponseEntity<TicketResponse> {
        return try {
            val ticket = ticketService.createTicket(request)
            ResponseEntity.status(HttpStatus.CREATED).body(ticket)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping
    fun getAllTickets(): ResponseEntity<List<TicketResponse>> {
        return ResponseEntity.ok(ticketService.getAllTickets())
    }

    @GetMapping("/{folio}")
    fun getTicketByFolio(@PathVariable folio: String): ResponseEntity<TicketResponse> {
        val ticket = ticketService.getTicketByFolio(folio)
        return if (ticket != null) {
            ResponseEntity.ok(ticket)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/user/{userId}")
    fun getTicketsByUser(@PathVariable userId: Int): ResponseEntity<List<TicketResponse>> {
        return ResponseEntity.ok(ticketService.getTicketsByUser(userId))
    }

    @GetMapping("/search/by-date")
    fun getTicketsByDateRange(
        @RequestParam startDate: String,
        @RequestParam endDate: String
    ): ResponseEntity<List<TicketResponse>> {
        val start = LocalDateTime.parse(startDate)
        val end = LocalDateTime.parse(endDate)
        return ResponseEntity.ok(ticketService.getTicketsByDateRange(start, end))
    }
}
