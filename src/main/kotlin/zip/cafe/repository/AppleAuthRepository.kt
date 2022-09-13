package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import zip.cafe.entity.auth.AppleAuth

interface AppleAuthRepository : JpaRepository<AppleAuth, Long> {
    fun findByAppleId(appleId: String): AppleAuth?
}
