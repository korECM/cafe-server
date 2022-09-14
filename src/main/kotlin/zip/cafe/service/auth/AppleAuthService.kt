package zip.cafe.service.auth

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import zip.cafe.entity.auth.AppleAuth
import zip.cafe.entity.member.Member
import zip.cafe.repository.AppleAuthRepository
import zip.cafe.repository.MemberRepository
import zip.cafe.service.auth.dto.AppleAuthKeys
import zip.cafe.service.auth.dto.AppleIdentityTokenHeader
import java.security.PublicKey
import java.util.*


private const val appleLoginFailMsg = "애플 로그인에 실패했습니다"

@Service
class AppleAuthService(
    private val appleAuthRepository: AppleAuthRepository,
    private val memberRepository: MemberRepository,
    private val restTemplate: RestTemplate,
    private val objectMapper: ObjectMapper,
    @Value("\${apple.clientId}")
    private val appleClientId: String
) {
    fun findMemberIdByAppleIdentityToken(identityToken: String, nickname: String): Long {
        val appleUserId = getUserId(identityToken)
        val foundAppleAuth = appleAuthRepository.findByAppleId(appleUserId)
        if (foundAppleAuth != null) {
            return foundAppleAuth.member.id
        }
        val newMember = saveNewAppleAccount(appleUserId, nickname)
        return newMember.id
    }

    private fun getUserId(identityToken: String): String {
        val publicKey = getPublicKey(identityToken)
        val claims = parseIdentityToken(publicKey, identityToken)
        return claims.subject
    }

    private fun getPublicKey(identityToken: String): PublicKey {
        val header = getHeaderFromIdentityToken(identityToken)
        return getAuthPublicKey(header.kid, header.alg)
    }

    private fun getHeaderFromIdentityToken(identityToken: String): AppleIdentityTokenHeader {
        val split = identityToken.split('.')
        if (split.size != 3) {
            throw RuntimeException(appleLoginFailMsg)
        }
        val header = String(Base64.getDecoder().decode(split[0]))
        return objectMapper.readValue(header, AppleIdentityTokenHeader::class.java)
    }

    private fun getAuthPublicKey(kid: String, alg: String): PublicKey {
        return getAppleAuthKeys()
            .getMatchedKey(kid, alg)
            ?.getPublicKey() ?: throw RuntimeException(appleLoginFailMsg)
    }

    private fun getAppleAuthKeys(): AppleAuthKeys {
        val host = "https://appleid.apple.com/auth/keys"
        return restTemplate.getForEntity(host, AppleAuthKeys::class.java).body ?: throw RuntimeException(appleLoginFailMsg)
    }

    private fun parseIdentityToken(publicKey: PublicKey, identityToken: String): Claims {
        val claimsJws = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken)
        println(claimsJws)
        println(claimsJws.body)
        if (claimsJws.body.issuer != "https://appleid.apple.com") throw RuntimeException(appleLoginFailMsg)
        if (claimsJws.body.audience != appleClientId) throw RuntimeException(appleLoginFailMsg)
        if (claimsJws.body.expiration.before(Date())) throw RuntimeException(appleLoginFailMsg)
        return claimsJws.body
    }

    @Transactional
    protected fun saveNewAppleAccount(appleUserId: String, nickname: String): Member {
        val member = Member(nickname = nickname)
        val appleAuth = AppleAuth(appleUserId, member)
        memberRepository.save(member)
        appleAuthRepository.save(appleAuth)
        return member
    }
}
