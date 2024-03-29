package zip.cafe.service.auth

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import zip.cafe.entity.auth.KakaoAuth
import zip.cafe.entity.member.Member
import zip.cafe.repository.KakaoAuthRepository
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.findByKakaoUserId
import zip.cafe.service.auth.dto.KakaoTokenInfo
import zip.cafe.service.auth.dto.KakaoUserInfo
import zip.cafe.util.logger

private const val kakaoHost = "https://kapi.kakao.com"
private const val kakaoLoginFailMsg = "카카오 로그인에 실패했습니다"


@Service
class KakaoAuthService(
    private val kakaoAuthRepository: KakaoAuthRepository,
    private val memberRepository: MemberRepository,
    private val restTemplate: RestTemplate,
) {
    fun findMemberIdByKakaoAccessToken(accessToken: String): Long {
        val kakaoUserId = getUserId(accessToken)
        val foundKakaoAuth = kakaoAuthRepository.findByKakaoUserId(kakaoUserId)
        if (foundKakaoAuth != null) {
            return foundKakaoAuth.member.id
        }
        val userInfo = getUserInfo(accessToken)
        val newMember = saveNewKakaoAccount(userInfo.id, userInfo.profile.nickname, userInfo.profile.profileImageURL)
        return newMember.id
    }

    fun getUserId(accessToken: String): Long {
        val host = "$kakaoHost/v1/user/access_token_info"
        val entity = createKakaoAuthHeader(accessToken)
        try {
            val response = restTemplate.exchange(host, HttpMethod.GET, entity, KakaoTokenInfo::class.java)
            println("response = $response")
            return response.body?.id ?: throw RuntimeException(kakaoLoginFailMsg)
        } catch (e: Throwable) {
            logger().error("kakao get token info error", e)
            throw RuntimeException(kakaoLoginFailMsg)
        }
    }

    fun getUserInfo(accessToken: String): KakaoUserInfo {
        val host = "$kakaoHost/v2/user/me"
        val entity = createKakaoAuthHeader(accessToken)
        try {
            val response = restTemplate.exchange(host, HttpMethod.GET, entity, KakaoUserInfo::class.java)
            println("response = $response")
            return response.body ?: throw RuntimeException(kakaoLoginFailMsg)
        } catch (e: Throwable) {
            logger().error("kakao get user info error", e)
            throw RuntimeException(kakaoLoginFailMsg)
        }
    }

    @Transactional
    fun saveNewKakaoAccount(kakaoUserId: Long, nickname: String, profileImageURL: String): Member {
        val member = Member(nickname = nickname, profileImage = profileImageURL)
        val kakaoAuth = KakaoAuth(kakaoUserId, member)
        memberRepository.save(member)
        kakaoAuthRepository.save(kakaoAuth)
        return member
    }

    private fun createKakaoAuthHeader(accessToken: String) = HttpEntity<Nothing>(HttpHeaders().apply { setBearerAuth(accessToken) })
}


