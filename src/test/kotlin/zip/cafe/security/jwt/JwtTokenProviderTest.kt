package zip.cafe.security.jwt

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import java.util.*

class JwtTokenProviderTest : FreeSpec({

    val secretToken = "asdfa1243ljasdfalsjf"
    val tokenValidityInSecond = 3600L * 1000

    lateinit var jwtTokenProvider: JwtTokenProvider

    beforeEach {
        jwtTokenProvider = JwtTokenProvider(secretToken = secretToken, tokenValidityInMilliSecond = tokenValidityInSecond)
    }

    "검증" - {
        "생성한 토큰은 검증에 통과한다" {
            // given
            val token = jwtTokenProvider.createToken(1L, Date())
            // when
            val result = jwtTokenProvider.isInvalidToken(token)
            // then
            result shouldBe true
        }
        "만약 다른 secret을 가지는 jwtTokenProvider로 생성한 토큰을 검증 시도하면 실패한다" {
            // given
            val anotherProvider = JwtTokenProvider(secretToken = secretToken + "asdf", tokenValidityInMilliSecond = tokenValidityInSecond)
            val token = anotherProvider.createToken(1L, Date())
            // when
            val result = jwtTokenProvider.isInvalidToken(token)
            // then
            result shouldBe false
        }
        "만약 jwt token이 아닌 문자열을 검증 시도하면 실패한다" {
            jwtTokenProvider.isInvalidToken("asdf") shouldBe false
            jwtTokenProvider.isInvalidToken("") shouldBe false
            jwtTokenProvider.isInvalidToken("1234") shouldBe false
        }
        "토큰이 만료된 후에 검증 시도하면 실패한다" {
            // given
            val anotherProvider = JwtTokenProvider(secretToken = secretToken, tokenValidityInMilliSecond = 1)
            val token = anotherProvider.createToken(1L, Date())
            // when
            delay(10L)
            val result = anotherProvider.isInvalidToken(token)
            // then
            result shouldBe false
        }
    }

    "토큰에서 userPK를 얻어올 수 있다" {
        // given
        val userPk = 1L
        val token = jwtTokenProvider.createToken(userPk, Date())
        // when
        val findUserPk = jwtTokenProvider.getUserPk(token)
        // then
        findUserPk shouldBe userPk
    }
})
