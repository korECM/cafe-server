package zip.cafe.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.findOneById
import zip.cafe.security.jwt.JwtTokenProvider
import zip.cafe.seeds.createMember
import zip.cafe.service.auth.AuthService
import java.util.*

class AuthServiceTest : FreeSpec({

    val jwtTokenProvider = mockk<JwtTokenProvider>(relaxed = true)
    val memberRepository = mockk<MemberRepository>(relaxed = true)
    val authService = AuthService(jwtTokenProvider = jwtTokenProvider, memberRepository = memberRepository)
    val tempJWTToken =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjUyMDExOTU0LCJleHAiOjE2ODM1NDc5NTR9.HeNl3BJdZuRrFT7Lhi9lHmCRIxPn5pxCWoIgDQONFFTIfA8NcTutGyqGfdyZ3JVtV8IYVVSf_SjLgMl3jFkvtQ"

    "jwtTokenProvider로부터 토큰을 생성해서 반환한다" {
        // given
        val memberId = 1L
        val nickname = "길동길동"
        val member = createMember(id = memberId, nickname = nickname)
        val now = Date()
        // mock
        every { jwtTokenProvider.createToken(memberId, nickname = nickname, at = now) } returns tempJWTToken
        every { memberRepository.findOneById(memberId) } returns member
        // when
        val generateToken = authService.generateToken(memberId, now)
        // then
        generateToken shouldBe tempJWTToken
        verify { jwtTokenProvider.createToken(memberId, nickname = nickname, at = now) }
    }

    afterTest { clearMocks(jwtTokenProvider) }
})
