package api.multipartes.dev.reports.paymentsMethod.service

import api.multipartes.dev.ticket.dto.PaymentMethodDto
import api.multipartes.dev.ticket.repository.TicketRepository
import org.springframework.stereotype.Service

@Service
class PaymentMethodService(private val _repo: TicketRepository) {

    fun getPaymentDistribution(): List<PaymentMethodDto> {
        return _repo.getPaymentStats()
    }
}