package api.multipartes.dev.endPoints.sales

import api.multipartes.dev.dtos.SaleRequest
import api.multipartes.dev.endPoints.parts.PartsRepo
import api.multipartes.dev.models.Sale
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional

@Service
class SalesService(
    private val saleRepository: SalesRepo,
    private val partRepository: PartsRepo
) {

    @CachePut(value = ["sales"], key = "#sale.id")
    @Transactional
    fun createSale(saleRequest: SaleRequest): Sale {
        val part = partRepository.findById(saleRequest.partId)
            .orElseThrow { IllegalArgumentException("Part not found") }

        val sale = Sale(
            part = part,
            customPartName = saleRequest.customPartName,
            quantity = saleRequest.quantity,
            price = saleRequest.price,
            paymentMethod = saleRequest.paymentMethod
        )

        return saleRepository.save(sale)
    }

    fun findById(id: Int): Optional<Sale> {
        return saleRepository.findById(id)
    }


    @Cacheable("sales")
    fun getAllSales(): List<Sale> {
        return saleRepository.findAll()
    }


    fun getSalesByPart(partId: Int): List<Sale> = saleRepository.findByPartId(partId)


    fun getSalesByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<Sale> =
        saleRepository.findByCreatedAtBetween(startDate, endDate)


    @CachePut(value = ["sales"], key = "#sale.id")
    @Transactional
    fun updateSale(id: Int, saleRequest: SaleRequest): Sale {
        val sale = saleRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Sale not found") }

        val part = partRepository.findById(saleRequest.partId)
            .orElseThrow { IllegalArgumentException("Part not found") }

        val updatedSale = sale.copy(
            part = part,
            customPartName = saleRequest.customPartName ?: sale.customPartName,
            quantity = saleRequest.quantity,
            price = saleRequest.price,
            paymentMethod = saleRequest.paymentMethod
        )

        return saleRepository.save(updatedSale)
    }


    @Transactional
    fun deleteSale(id: Int) {
        val sale = saleRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Sale not found") }
        saleRepository.delete(sale)
    }

}