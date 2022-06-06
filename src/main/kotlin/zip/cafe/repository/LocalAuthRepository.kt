package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import zip.cafe.entity.auth.LocalAuth

fun LocalAuthRepository.findByAuthId(id: String) = this.findByLocalId(id)

interface LocalAuthRepository : JpaRepository<LocalAuth, Long> {
    fun findByLocalId(localId: String): LocalAuth?
}
