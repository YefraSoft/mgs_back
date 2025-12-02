package api.multipartes.dev.sales.service

import api.multipartes.dev.models.Sale
import api.multipartes.dev.sales.repository.SalesRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.*

@Service
class SalesService(
    private val _repo: SalesRepository
) {

    fun findById(id: Int): Optional<Sale> {
        return _repo.findById(id)
    }

    @Cacheable("sales")
    fun getAll(): List<Sale> {
        return _repo.findAll()
    }

    fun getByPart(partId: Int): List<Sale> {
        return _repo.findByPartId(partId);
    }

    fun getByTicked(folio: String): List<Sale> {
        return _repo.findByTicketFolio(folio);
    }

}