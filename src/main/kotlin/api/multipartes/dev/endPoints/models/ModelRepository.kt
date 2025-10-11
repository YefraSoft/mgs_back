package api.multipartes.dev.endPoints.models

import api.multipartes.dev.models.Model
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ModelRepository : JpaRepository<Model, Int> {
    fun findByBrandId(brandId: Int): List<Model>
    fun findByYear(year: Int): List<Model>
    fun findByNameContainingIgnoreCase(name: String): List<Model>
    fun findByBrandIdAndYear(brandId: Int, year: Int): List<Model>
}
