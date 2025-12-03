package api.multipartes.dev.ticket.controller

import api.multipartes.dev.ticket.dto.GetTicketResponse
import api.multipartes.dev.ticket.dto.MakeResponse
import api.multipartes.dev.ticket.dto.TicketRequest
import api.multipartes.dev.ticket.dto.UpdateTicketRequest

import api.multipartes.dev.ticket.service.TicketService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/tickets")
class TicketController(
    private val _service: TicketService
) {

    @PostMapping
    fun createTicket(@RequestBody request: TicketRequest): ResponseEntity<MakeResponse> {
        return try {
            val ticket = _service.make(request)
            ResponseEntity.status(HttpStatus.CREATED).body(ticket)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: IllegalStateException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PutMapping("/{folio}")
    fun updateTicket(
        @PathVariable folio: String,
        @RequestBody request: UpdateTicketRequest
    ): ResponseEntity<GetTicketResponse> {
        return try {
            ResponseEntity.ok(_service.update(folio, request))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @DeleteMapping("/{folio}")
    fun deleteTicket(@PathVariable folio: String): ResponseEntity<Void> {
        return try {
            _service.delete(folio)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAllTickets(): ResponseEntity<List<GetTicketResponse>> {
        return ResponseEntity.ok(_service.getAll())
    }

    @GetMapping("/{folio}")
    fun getTicketByFolio(@PathVariable folio: String): ResponseEntity<GetTicketResponse> {
        val ticket = _service.getByFolio(folio)
        return if (ticket != null) {
            ResponseEntity.ok(ticket)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/user/{userId}")
    fun getTicketsByUser(@PathVariable userId: Int): ResponseEntity<List<GetTicketResponse>> {
        return ResponseEntity.ok(_service.getByUser(userId))
    }

    @GetMapping("/search/by-date")
    fun getTicketsByDateRange(
        @RequestParam startDate: String,
        @RequestParam endDate: String
    ): ResponseEntity<List<GetTicketResponse>> {
        val start = LocalDateTime.parse(startDate)
        val end = LocalDateTime.parse(endDate)
        return ResponseEntity.ok(_service.getTicketsByDateRange(start, end))
    }
}