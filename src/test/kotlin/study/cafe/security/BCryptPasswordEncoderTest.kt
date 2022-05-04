package study.cafe.security

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class BCryptPasswordEncoderTest : FreeSpec({

    lateinit var passwordEncoder: BCryptPasswordEncoder

    beforeEach {
        passwordEncoder = BCryptPasswordEncoder()
    }

    "encode 함수는 기존 값과 다른 인코딩된 값을 반환한다" {
        // given
        val orgPassword = "testPw"
        // when
        val encodedResult = passwordEncoder.encode(orgPassword)
        // then
        encodedResult shouldNotBe orgPassword
    }

    "encode한 값을 match하면 true를 반환한다" {
        // given
        val orgPassword = "testPw"
        // when
        val encodedResult = passwordEncoder.encode(orgPassword)
        // then
        passwordEncoder.matches(orgPassword, encodedResult) shouldBe true
        passwordEncoder.matches("otherPw", encodedResult) shouldBe false
    }
})
