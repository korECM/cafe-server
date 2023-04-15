package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import zip.cafe.entity.auth.LocalAuth

fun LocalAuthRepository.findByAuthId(id: String) = this.findByLocalId(id)

interface LocalAuthRepository : JpaRepository<LocalAuth, Long> {
    @Query("select a from LocalAuth a where a.localId = :localId and a.isDeleted = false")
    fun findByLocalId(localId: String): LocalAuth?

    @Query("select exists(select a from LocalAuth a where a.localId = :localId and a.isDeleted = false)")
    fun existsByLocalIdIs(localId: String): Boolean

    @Query("select a from LocalAuth a where a.member.id = :memberId and a.isDeleted = false")
    fun findByMember(memberId: Long): LocalAuth?
}
