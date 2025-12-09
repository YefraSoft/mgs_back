package api.multipartes.dev.reports.partsCategory.service

import api.multipartes.dev.parts.dto.PartsCategoryDto
import api.multipartes.dev.parts.repository.PartsRepository
import org.springframework.stereotype.Service

@Service
class PartsCategoryService(private val _repo: PartsRepository) {

    fun getDistribution(): List<PartsCategoryDto> {
        return _repo.getCategoryDistribution()
    }
}