package zip.cafe.service

import org.springframework.stereotype.Service
import zip.cafe.security.jwt.JwtTokenProvider
import java.util.*

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun generateToken(memberId: Long, at: Date): String {
        return jwtTokenProvider.createToken(memberId, at)
    }
}
