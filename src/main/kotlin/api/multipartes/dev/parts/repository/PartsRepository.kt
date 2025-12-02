package api.multipartes.dev.parts.repository

import api.multipartes.dev.models.Part
import org.springframework.data.jpa.repository.JpaRepository

interface PartsRepository : JpaRepository<Part, Int> {
}