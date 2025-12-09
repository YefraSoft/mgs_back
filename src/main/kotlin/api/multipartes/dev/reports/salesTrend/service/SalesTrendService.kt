package api.multipartes.dev.reports.salesTrend.service

import api.multipartes.dev.ticket.dto.SalesTrend
import api.multipartes.dev.ticket.repository.TicketRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class SalesTrendService(private val _repo: TicketRepository) {

    @Cacheable("sales-trend")
    fun getMonthlySalesTrend(): List<SalesTrend> {
        return _repo.findMonthlySales() ?: emptyList()
    }
}