package zip.cafe.security.jwt

import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import zip.cafe.util.logger
import zip.cafe.util.plus
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val secretToken: String,
    @Value("\${jwt.token-validity-in-secnods}")
    private val tokenValidityInSecond: Long
) {
    private val key: String = Base64.getEncoder().encodeToString(secretToken.toByteArray(Charsets.UTF_8))

    fun createToken(userPk: Long, at: Date): String {
        val claims = Jwts.claims().setSubject(userPk.toString())
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(at)
            .setExpiration(at + tokenValidityInSecond * 1000)
            .signWith(SignatureAlgorithm.HS512, key)
            .compact()
    }

    fun getUserPk(token: String): Long {
        val claims = parseClaims(token)
        requireNotNull(claims) { "올바른 JWT 토큰이 아니라 값을 추출할 수 없습니다" }
        try {
            return claims.subject.toLong()
        } catch (e: java.lang.NumberFormatException) {
            throw IllegalArgumentException("올바른 JWT 토큰이 아니라 값을 추출할 수 없습니다")
        }
    }

    fun validateToken(token: String): Boolean {
        try {
            parseClaims(token)
            return true
        } catch (e: SecurityException) {
            logger().info("잘못된 JWT 서명")
        } catch (e: MalformedJwtException) {
            logger().info("잘못된 JWT 서명")
        } catch (e: ExpiredJwtException) {
            logger().info("만료된 JWT 토큰")
        } catch (e: UnsupportedJwtException) {
            logger().info("지원되지 않는 JWT 토큰")
        } catch (e: IllegalArgumentException) {
            logger().info("잘못된 JWT 토큰")
        }
        return false
    }

    private fun parseClaims(token: String): Claims? = Jwts.parser().setSigningKey(key).parseClaimsJws(token).body
}
