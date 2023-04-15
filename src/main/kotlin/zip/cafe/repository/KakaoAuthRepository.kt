package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import zip.cafe.entity.auth.KakaoAuth

fun KakaoAuthRepository.findByKakaoUserId(id: Long) = this.findByKakaoId(id)

interface KakaoAuthRepository : JpaRepository<KakaoAuth, Long> {
    @Query("select a from KakaoAuth a where a.kakaoId = :kakaoId and a.isDeleted = false")
    fun findByKakaoId(kakaoId: Long): KakaoAuth?

    @Query("select a from KakaoAuth a where a.member.id = :memberId and a.isDeleted = false")
    fun findByMember(memberId: Long): KakaoAuth?
}
