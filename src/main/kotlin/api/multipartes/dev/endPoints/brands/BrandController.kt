package api.multipartes.dev.endPoints.brands

import api.multipartes.dev.dtos.BrandResponse
import api.multipartes.dev.dtos.CreateBrandRequest
import api.multipartes.dev.dtos.UpdateBrandRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/brands")
class BrandController(
    private val brandService: BrandService
) {

    @GetMapping
    fun getAllBrands(): ResponseEntity<List<BrandResponse>> {
        return ResponseEntity.ok(brandService.getAllBrands())
    }

    @GetMapping("/{id}")
    fun getBrandById(@PathVariable id: Int): ResponseEntity<BrandResponse> {
        return try {
            ResponseEntity.ok(brandService.getBrandById(id))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/search")
    fun searchBrands(@RequestParam name: String): ResponseEntity<List<BrandResponse>> {
        return ResponseEntity.ok(brandService.searchBrandsByName(name))
    }

    @PostMapping
    fun createBrand(@RequestBody request: CreateBrandRequest): ResponseEntity<Any> {
        return try {
            val brand = brandService.createBrand(request)
            ResponseEntity.status(HttpStatus.CREATED).body(brand)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}")
    fun updateBrand(
        @PathVariable id: Int,
        @RequestBody request: UpdateBrandRequest
    ): ResponseEntity<Any> {
        return try {
            val brand = brandService.updateBrand(id, request)
            ResponseEntity.ok(brand)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @DeleteMapping("/{id}")
    fun deleteBrand(@PathVariable id: Int): ResponseEntity<Any> {
        return try {
            brandService.deleteBrand(id)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}
