package api.multipartes.dev.parts.controller

import api.multipartes.dev.dtos.PartResponse
import api.multipartes.dev.parts.service.PartsService
import api.multipartes.dev.models.Part
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/parts")
class PartsController(private val service: PartsService) {

    @GetMapping
    fun getAllParts(): ResponseEntity<List<PartResponse>> = ResponseEntity.ok(service.findAll())

    @GetMapping("/{id}")
    fun getPartById(@PathVariable id: Int): ResponseEntity<PartResponse> {
        val part = service.findById(id)
        return if (part != null) ResponseEntity.ok(part) else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createPart(@RequestBody part: Part): ResponseEntity<Part> =
        ResponseEntity.status(HttpStatus.CREATED).body(service.save(part))

    @PutMapping("/{id}")
    fun updatePart(@PathVariable id: Int, @RequestBody part: Part): ResponseEntity<Part> {
        val updatedPart = service.update(id, part)
        return if (updatedPart != null) ResponseEntity.ok(updatedPart) else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deletePart(@PathVariable id: Int): ResponseEntity<Void> {
        return try {
            service.deleteById(id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }
}