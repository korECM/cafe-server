package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import zip.cafe.entity.auth.KakaoAuth

fun KakaoAuthRepository.findByKakaoUserId(id: Long) = this.findByKakaoId(id)

interface KakaoAuthRepository : JpaRepository<KakaoAuth, Long> {
    fun findByKakaoId(kakaoId: Long): KakaoAuth?
}
