package api.multipartes.dev.endPoints.brands

import api.multipartes.dev.models.VehicleBrands
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandRepository : JpaRepository<VehicleBrands, Int> {
    fun existsByName(name: String): Boolean
    fun findByNameContainingIgnoreCase(name: String): List<VehicleBrands>
}
