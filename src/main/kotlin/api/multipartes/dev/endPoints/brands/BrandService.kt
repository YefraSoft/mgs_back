package api.multipartes.dev.endPoints.brands

import api.multipartes.dev.dtos.BrandResponse
import api.multipartes.dev.dtos.CreateBrandRequest
import api.multipartes.dev.dtos.UpdateBrandRequest
import api.multipartes.dev.models.VehicleBrands
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class BrandService(
    private val brandRepository: BrandRepository
) {

    @Cacheable("brands")
    fun getAllBrands(): List<BrandResponse> {
        return brandRepository.findAll().map { brand ->
            BrandResponse(
                id = brand.id!!,
                name = brand.name
            )
        }
    }

    fun getBrandById(id: Int): BrandResponse {
        val brand = brandRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Brand with ID $id not found") }
        
        return BrandResponse(
            id = brand.id!!,
            name = brand.name
        )
    }

    fun searchBrandsByName(name: String): List<BrandResponse> {
        return brandRepository.findByNameContainingIgnoreCase(name).map { brand ->
            BrandResponse(
                id = brand.id!!,
                name = brand.name
            )
        }
    }

    @CacheEvict(value = ["brands"], allEntries = true)
    fun createBrand(request: CreateBrandRequest): BrandResponse {
        if (brandRepository.existsByName(request.name)) {
            throw IllegalArgumentException("Brand with name '${request.name}' already exists")
        }

        val newBrand = VehicleBrands(
            name = request.name
        )

        val savedBrand = brandRepository.save(newBrand)
        
        return BrandResponse(
            id = savedBrand.id!!,
            name = savedBrand.name
        )
    }

    @CacheEvict(value = ["brands"], allEntries = true)
    fun updateBrand(id: Int, request: UpdateBrandRequest): BrandResponse {
        val existingBrand = brandRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Brand with ID $id not found") }

        // Verificar si el nuevo nombre ya existe (excepto si es el mismo brand)
        if (brandRepository.existsByName(request.name) && existingBrand.name != request.name) {
            throw IllegalArgumentException("Brand with name '${request.name}' already exists")
        }

        val updatedBrand = existingBrand.copy(
            name = request.name
        )

        val savedBrand = brandRepository.save(updatedBrand)
        
        return BrandResponse(
            id = savedBrand.id!!,
            name = savedBrand.name
        )
    }

    @CacheEvict(value = ["brands", "models"], allEntries = true)
    fun deleteBrand(id: Int) {
        if (!brandRepository.existsById(id)) {
            throw IllegalArgumentException("Brand with ID $id not found")
        }
        brandRepository.deleteById(id)
    }
}
