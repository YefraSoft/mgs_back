package api.multipartes.dev.reports.adquisitionPurchase.service

import api.multipartes.dev.reports.adquisitionPurchase.dtos.AmountAcquisitionsPurchasesDto
import api.multipartes.dev.reports.adquisitionPurchase.repository.AcquisitionPurchaseRepository
import org.springframework.stereotype.Service

@Service
class AcquisitionPurchaseService(private val _repo: AcquisitionPurchaseRepository) {

    fun getMonthlyReport(): List<AmountAcquisitionsPurchasesDto> {
        return _repo.getMonthlyReport()
    }
}