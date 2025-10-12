package api.multipartes.dev.endPoints.parts

import api.multipartes.dev.dtos.PartResponse
import api.multipartes.dev.models.Part
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class PartsService(private val repo: PartsRepo) {

    @Cacheable("parts")
    fun findAll(): List<PartResponse> {
        return repo.findAll().map { part ->
            PartResponse(
                id = part.id!!,
                code = part.code,
                name = part.name,
                side = part.side.name,
                category = part.categoryType.name,
                color = part.color,
                price = part.price,
                quantity = part.quantity
            )
        }
    }

    fun findById(id: Int): PartResponse? {
        val part = repo.findById(id).orElse(null) ?: return null
        return PartResponse(
            id = part.id!!,
            code = part.code,
            name = part.name,
            side = part.side.name,
            category = part.categoryType.name,
            color = part.color,
            price = part.price,
            quantity = part.quantity
        )
    }

    @CachePut(value = ["parts"], key = "#part.id")
    fun save(part: Part): Part = repo.save(part)

    @CachePut(value = ["parts"], key = "#id")
    fun update(id: Int, part: Part): Part? {
        if (repo.existsById(id)) {
            return repo.save(part.copy(id = id))
        }
        return null
    }

    @CacheEvict(value = ["parts"], key = "#id")
    fun deleteById(id: Int) = repo.deleteById(id)
}