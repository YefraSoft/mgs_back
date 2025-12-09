package api.multipartes.dev.reports.warrantyHistory.service


import api.multipartes.dev.reports.warrantyHistory.dto.WarrantyHistoryDto
import api.multipartes.dev.reports.warrantyHistory.repository.WarrantyHistoryRepository
import org.springframework.stereotype.Service

@Service
class WarrantyHistoryService(private val _repo: WarrantyHistoryRepository) {

    fun getWarrantyHistory():List<WarrantyHistoryDto>{
        return _repo.getWarrantyReport()
    }

}