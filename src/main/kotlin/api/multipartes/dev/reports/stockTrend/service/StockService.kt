package api.multipartes.dev.reports.stockTrend.service


import api.multipartes.dev.parts.dto.StockDto
import api.multipartes.dev.parts.repository.PartsRepository
import org.springframework.stereotype.Service

@Service
class StockService(private val _repo: PartsRepository) {

    fun getLowStock(threshold: Int): List<StockDto> {
        return _repo.findByQuantity(threshold)
    }
}