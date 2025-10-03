package api.multipartes.dev.endPoints.parts

import api.multipartes.dev.models.Part
import org.springframework.data.jpa.repository.JpaRepository

interface PartsRepo : JpaRepository<Part, Int> {
}