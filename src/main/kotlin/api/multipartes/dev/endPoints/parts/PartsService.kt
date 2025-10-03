package api.multipartes.dev.endPoints.parts

import api.multipartes.dev.models.Part
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class PartsService(private val repo: PartsRepo) {

    @Cacheable("parts")
    fun findAll(): List<Part> = repo.findAll()
    fun findById(id: Int): Part? = repo.findById(id).orElse(null)

    @CachePut(value = ["parts"], key = "#part.id")
    fun save(part: Part): Part = repo.save(part)

    @CachePut(value = ["parts"], key = "#part.id")
    fun update(id: Int, part: Part): Part? {
        if (repo.existsById(id)) {
            return repo.save(part.copy(id = id))
        }
        return null
    }
    fun deleteById(id: Int) = repo.deleteById(id)
}