package api.multipartes.dev.warranty.repository

import api.multipartes.dev.enums.WarrantyStatus
import api.multipartes.dev.models.Warranty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface WarrantyRepository : JpaRepository<Warranty, Int> {
    fun findBySaleId(saleId: Int): Warranty?
    fun findByStatus(status: WarrantyStatus): List<Warranty>
    fun findByExpirationDateBefore(date: LocalDate): List<Warranty>
    fun findByExpirationDateAfter(date: LocalDate): List<Warranty>
}
