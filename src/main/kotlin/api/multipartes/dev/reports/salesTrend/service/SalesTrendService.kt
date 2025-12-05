package api.multipartes.dev.reports.salesTrend.service

import api.multipartes.dev.reports.salesTrend.dto.SalesTrend
import api.multipartes.dev.reports.salesTrend.repository.SalesTrendRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class SalesTrendService(private val _repo: SalesTrendRepository) {

    @Cacheable("sales-trend")
    fun getMonthlySalesTrend(): List<SalesTrend> {
        return _repo.findMonthlySales() ?: emptyList()
    }
}