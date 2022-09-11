package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import zip.cafe.entity.auth.AppleAuth

fun AppleAuthRepository.findByAppleId(id: Long) = this.findByAppleId(id)

interface AppleAuthRepository : JpaRepository<AppleAuth, Long> {
    fun findByAppleId(appleId: Long): AppleAuth?
}
