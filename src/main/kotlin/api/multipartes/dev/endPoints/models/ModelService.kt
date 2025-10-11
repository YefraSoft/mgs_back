package api.multipartes.dev.endPoints.models

import api.multipartes.dev.dtos.CreateModelRequest
import api.multipartes.dev.dtos.ModelResponse
import api.multipartes.dev.dtos.UpdateModelRequest
import api.multipartes.dev.endPoints.brands.BrandRepository
import api.multipartes.dev.enums.TransmissionType
import api.multipartes.dev.models.Model
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class ModelService(
    private val modelRepository: ModelRepository,
    private val brandRepository: BrandRepository
) {

    @Cacheable("models")
    fun getAllModels(): List<ModelResponse> {
        return modelRepository.findAll().map { model ->
            mapToResponse(model)
        }
    }

    fun getModelById(id: Int): ModelResponse {
        val model = modelRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Model with ID $id not found") }
        return mapToResponse(model)
    }

    fun getModelsByBrand(brandId: Int): List<ModelResponse> {
        if (!brandRepository.existsById(brandId)) {
            throw IllegalArgumentException("Brand with ID $brandId not found")
        }
        
        return modelRepository.findByBrandId(brandId).map { model ->
            mapToResponse(model)
        }
    }

    fun getModelsByYear(year: Int): List<ModelResponse> {
        return modelRepository.findByYear(year).map { model ->
            mapToResponse(model)
        }
    }

    fun searchModelsByName(name: String): List<ModelResponse> {
        return modelRepository.findByNameContainingIgnoreCase(name).map { model ->
            mapToResponse(model)
        }
    }

    fun getModelsByBrandAndYear(brandId: Int, year: Int): List<ModelResponse> {
        if (!brandRepository.existsById(brandId)) {
            throw IllegalArgumentException("Brand with ID $brandId not found")
        }
        
        return modelRepository.findByBrandIdAndYear(brandId, year).map { model ->
            mapToResponse(model)
        }
    }

    @CacheEvict(value = ["models"], allEntries = true)
    fun createModel(request: CreateModelRequest): ModelResponse {
        val brand = brandRepository.findById(request.brandId)
            .orElseThrow { IllegalArgumentException("Brand with ID ${request.brandId} not found") }

        val transmission = request.transmission?.let {
            try {
                TransmissionType.valueOf(it.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid transmission type. Must be AUTOMATIC or STANDARD")
            }
        }

        val newModel = Model(
            brand = brand,
            serialNumber = request.serialNumber,
            name = request.name,
            year = request.year,
            transmission = transmission,
            engine = request.engine,
            vehicleClass = request.vehicleClass
        )

        val savedModel = modelRepository.save(newModel)
        return mapToResponse(savedModel)
    }

    @CacheEvict(value = ["models"], allEntries = true)
    fun updateModel(id: Int, request: UpdateModelRequest): ModelResponse {
        val existingModel = modelRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Model with ID $id not found") }

        val brand = brandRepository.findById(request.brandId)
            .orElseThrow { IllegalArgumentException("Brand with ID ${request.brandId} not found") }

        val transmission = request.transmission?.let {
            try {
                TransmissionType.valueOf(it.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid transmission type. Must be AUTOMATIC or STANDARD")
            }
        }

        val updatedModel = existingModel.copy(
            brand = brand,
            serialNumber = request.serialNumber,
            name = request.name,
            year = request.year,
            transmission = transmission,
            engine = request.engine,
            vehicleClass = request.vehicleClass
        )

        val savedModel = modelRepository.save(updatedModel)
        return mapToResponse(savedModel)
    }

    @CacheEvict(value = ["models"], allEntries = true)
    fun deleteModel(id: Int) {
        if (!modelRepository.existsById(id)) {
            throw IllegalArgumentException("Model with ID $id not found")
        }
        modelRepository.deleteById(id)
    }

    private fun mapToResponse(model: Model): ModelResponse {
        return ModelResponse(
            id = model.id!!,
            brandId = model.brand.id!!,
            brandName = model.brand.name,
            serialNumber = model.serialNumber,
            name = model.name,
            year = model.year,
            transmission = model.transmission?.name,
            engine = model.engine,
            vehicleClass = model.vehicleClass
        )
    }
}
