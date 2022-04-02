package study.cafe.service

import org.springframework.stereotype.Service
import study.cafe.security.jwt.JwtTokenProvider
import java.util.*

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun generateToken(memberId: Long): String {
        return jwtTokenProvider.createToken(memberId, Date())
    }
}
