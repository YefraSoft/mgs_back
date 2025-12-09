package api.multipartes.dev.reports.warrantyHistory.repository

import api.multipartes.dev.reports.warrantyHistory.dto.WarrantyHistoryDto
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class WarrantyHistoryRepository(private val jdbcTemplate: JdbcTemplate) {

    private val warrantyReport =
        """
            SELECT 
                w.status,
                wc.claim_type AS claimType,
                COUNT(*) AS total
            FROM warranty w
            INNER JOIN warranty_claim wc ON w.id = wc.warranty_id
            GROUP BY w.status, wc.claim_type
            ORDER BY w.status, wc.claim_type
        """;

    fun getWarrantyReport(): List<WarrantyHistoryDto> {
        return jdbcTemplate.query(warrantyReport) { rs, _ ->
            WarrantyHistoryDto(
                status = rs.getString("status"),
                claimType = rs.getString("claimType"),
                total = rs.getInt("total")
            )
        }
    }
}