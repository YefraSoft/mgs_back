package api.multipartes.dev.reports.adquisitionPurchase.repository

import api.multipartes.dev.reports.adquisitionPurchase.dtos.AmountAcquisitionsPurchasesDto
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AcquisitionPurchaseRepository(private val jdbc: JdbcTemplate) {

    private val monthlyReport =
        """
            SELECT periodo, ( SELECT SUM(purchase_cost)
                                FROM purchase_vehicles
                                WHERE
                                DATE_FORMAT(purchase_date, '%Y-%M') 
                                = periodo) AS totalCompras,
                            ( SELECT SUM(total) 
                                FROM tickets
                                WHERE DATE_FORMAT(date, '%Y-%M') 
                                = periodo) AS totalVentas
            FROM (SELECT DATE_FORMAT(purchase_date, '%Y-%M') AS periodo
                    FROM purchase_vehicles
                    UNION 
                  SELECT DATE_FORMAT(date, '%Y-%M') AS periodo
                    FROM tickets) AS meses
            ORDER BY periodo;
        """;

    fun getMonthlyReport(): List<AmountAcquisitionsPurchasesDto> {
        return jdbc.query(monthlyReport) { rs, _ ->
            AmountAcquisitionsPurchasesDto(
                periodo = rs.getString("periodo"),
                totalCompras = rs.getDouble("totalCompras"),
                totalVentas = rs.getDouble("totalVentas")
            )
        }

    }
}