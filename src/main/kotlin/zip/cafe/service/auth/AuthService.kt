package zip.cafe.service.auth

import org.springframework.stereotype.Service
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.findOneById
import zip.cafe.security.jwt.JwtTokenProvider
import java.util.*

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository,
) {
    fun generateToken(memberId: Long, at: Date): String {
        val member = memberRepository.findOneById(memberId)
        return jwtTokenProvider.createToken(memberId, nickname = member.nickname, at = at)
    }
}
