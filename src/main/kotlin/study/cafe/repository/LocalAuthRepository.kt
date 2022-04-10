package study.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import study.cafe.entity.auth.LocalAuth

fun LocalAuthRepository.findByAuthId(id: String) = this.findByLocalId(id)

interface LocalAuthRepository : JpaRepository<LocalAuth, Long> {
    fun findByLocalId(localId: String): LocalAuth?
}
