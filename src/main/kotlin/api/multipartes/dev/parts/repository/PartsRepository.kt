package api.multipartes.dev.parts.repository

import api.multipartes.dev.models.Part
import api.multipartes.dev.parts.dto.PartsCategoryDto
import api.multipartes.dev.parts.dto.StockDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PartsRepository : JpaRepository<Part, Int> {

    @Query(
        "SELECT category_type, COUNT(*) FROM parts GROUP BY category_type",
        nativeQuery = true
    )
    fun getCategoryDistribution(): List<PartsCategoryDto>

    fun findByQuantity(threshold: Int): List<StockDto>

}