package api.multipartes.dev.endPoints.models

import api.multipartes.dev.dtos.CreateModelRequest
import api.multipartes.dev.dtos.ModelResponse
import api.multipartes.dev.dtos.UpdateModelRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/models")
class ModelController(
    private val modelService: ModelService
) {

    @GetMapping
    fun getAllModels(): ResponseEntity<List<ModelResponse>> {
        return ResponseEntity.ok(modelService.getAllModels())
    }

    @GetMapping("/{id}")
    fun getModelById(@PathVariable id: Int): ResponseEntity<ModelResponse> {
        return try {
            ResponseEntity.ok(modelService.getModelById(id))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/search/by-brand/{brandId}")
    fun getModelsByBrand(@PathVariable brandId: Int): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(modelService.getModelsByBrand(brandId))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/search/by-year/{year}")
    fun getModelsByYear(@PathVariable year: Int): ResponseEntity<List<ModelResponse>> {
        return ResponseEntity.ok(modelService.getModelsByYear(year))
    }

    @GetMapping("/search/by-name")
    fun searchModelsByName(@RequestParam name: String): ResponseEntity<List<ModelResponse>> {
        return ResponseEntity.ok(modelService.searchModelsByName(name))
    }

    @GetMapping("/search/by-brand-and-year")
    fun getModelsByBrandAndYear(
        @RequestParam brandId: Int,
        @RequestParam year: Int
    ): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(modelService.getModelsByBrandAndYear(brandId, year))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PostMapping
    fun createModel(@RequestBody request: CreateModelRequest): ResponseEntity<Any> {
        return try {
            val model = modelService.createModel(request)
            ResponseEntity.status(HttpStatus.CREATED).body(model)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}")
    fun updateModel(
        @PathVariable id: Int,
        @RequestBody request: UpdateModelRequest
    ): ResponseEntity<Any> {
        return try {
            val model = modelService.updateModel(id, request)
            ResponseEntity.ok(model)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @DeleteMapping("/{id}")
    fun deleteModel(@PathVariable id: Int): ResponseEntity<Any> {
        return try {
            modelService.deleteModel(id)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}
