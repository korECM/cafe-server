package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import zip.cafe.entity.auth.AppleAuth

interface AppleAuthRepository : JpaRepository<AppleAuth, Long> {
    @Query("select a from AppleAuth a where a.appleId = :appleId and a.isDeleted = false")
    fun findByAppleId(appleId: String): AppleAuth?

    @Query("select a from AppleAuth a where a.member.id = :memberId and a.isDeleted = false")
    fun findByMember(memberId: Long): AppleAuth?
}
