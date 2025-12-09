package api.multipartes.dev.reports.paymentsMethod.controller

import api.multipartes.dev.ticket.dto.PaymentMethodDto
import api.multipartes.dev.reports.paymentsMethod.service.PaymentMethodService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/payment-trend")
class PaymentMethodController(private val _service: PaymentMethodService) {

    @GetMapping
    fun getPaymentTrend(): ResponseEntity<List<PaymentMethodDto>> {
        val result = _service.getPaymentDistribution()
        return if (result.isEmpty())
            ResponseEntity.noContent().build()
        else
            ResponseEntity.ok(result)
    }
}