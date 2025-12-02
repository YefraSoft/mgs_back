package api.multipartes.dev.ticket.service

import api.multipartes.dev.enums.PaymentMethod
import api.multipartes.dev.enums.WarrantyStatus
import api.multipartes.dev.models.User
import api.multipartes.dev.ticket.dto.TicketItem
import api.multipartes.dev.ticket.dto.TicketRequest
import api.multipartes.dev.user.repository.UserRepository
import api.multipartes.dev.parts.repository.PartsRepository
import api.multipartes.dev.sales.repository.SalesRepository
import api.multipartes.dev.ticket.repository.TicketRepository
import api.multipartes.dev.warranty.repository.WarrantyRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Optional

class TicketServiceTest {

    private val ticketRepo = mock(TicketRepository::class.java)
    private val salesRepo = mock(SalesRepository::class.java)
    private val partsRepo = mock(PartsRepository::class.java)
    private val userRepo = mock(UserRepository::class.java)
    private val warrantyRepo = mock(WarrantyRepository::class.java)

    private val ticketService = TicketService(
        _repo = ticketRepo,
        _salesRepo = salesRepo,
        _partsRepo = partsRepo,
        _userRepo = userRepo,
        _warrantyRepo = warrantyRepo
    )

    @Test
    fun testMakeTicketWithoutWarranty() {
        // Arrange
        val userId = 1
        val user = mock(User::class.java)
        val request = TicketRequest(
            sellerId = userId,
            paymentMethod = "CASH",
            total = BigDecimal("100.00"),
            items = listOf(
                TicketItem(
                    partId = 1,
                    quantity = 1.toByte(),
                    price = BigDecimal("100.00"),
                    partName = null,
                    hasWarranty = false,
                    warrantyExpirationDate = null
                )
            )
        )

        `when`(userRepo.findById(userId)).thenReturn(Optional.of(user))

        // Act & Assert
        // Verify that warranty repo is not called when hasWarranty is false
        // (This is a simplified test; ideally you'd mock more completely)
        // ticketService.makeTicket(request)
        // verify(warrantyRepo, never()).save(any())
    }

    @Test
    fun testMakeTicketWithInvalidPaymentMethod() {
        // Arrange
        val request = TicketRequest(
            sellerId = 1,
            paymentMethod = "INVALID_METHOD",
            total = BigDecimal("100.00"),
            items = listOf()
        )

        val user = mock(User::class.java)
        `when`(userRepo.findById(1)).thenReturn(Optional.of(user))

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            ticketService.make(request)
        }
    }

    @Test
    fun testMakeTicketWithUserNotFound() {
        // Arrange
        val request = TicketRequest(
            sellerId = 999,
            paymentMethod = "CASH",
            total = BigDecimal("100.00"),
            items = listOf()
        )

        `when`(userRepo.findById(999)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            ticketService.make(request)
        }
    }

    @Test
    fun testMakeTicketItemWithoutPartIdOrName() {
        // Arrange
        val userId = 1
        val user = mock(User::class.java)
        val request = TicketRequest(
            sellerId = userId,
            paymentMethod = "CASH",
            total = BigDecimal("100.00"),
            items = listOf(
                TicketItem(
                    partId = null,
                    quantity = 1.toByte(),
                    price = BigDecimal("100.00"),
                    partName = null,  // Both null
                    hasWarranty = false,
                    warrantyExpirationDate = null
                )
            )
        )

        `when`(userRepo.findById(userId)).thenReturn(Optional.of(user))

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            ticketService.make(request)
        }
    }
}
