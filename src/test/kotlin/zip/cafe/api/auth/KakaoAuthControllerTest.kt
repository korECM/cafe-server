package zip.cafe.api.auth

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import zip.cafe.api.auth.dto.KakaoSignInRequest
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.restdocs.STRING
import zip.cafe.api.utils.restdocs.requestFields
import zip.cafe.api.utils.restdocs.type
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.service.auth.AuthService
import zip.cafe.service.auth.KakaoAuthService

@WebMvcTest(KakaoAuthController::class)
class KakaoAuthControllerTest : WebMvcTestSpec() {

    @MockkBean
    private lateinit var kakaoAuthService: KakaoAuthService

    @MockkBean
    private lateinit var authService: AuthService

    init {
        "카카오 로그인 / 회원가입" {
            val accessToken = "JWjHXiVIlkxciAy_fTiEft3wDaAdHvOVcV_D6wwpCinI2gAAAYMNQf1c"

            every { kakaoAuthService.getUserInfo(accessToken) } just Runs

            val response = mockMvc.post("/auth/kakao/signIn") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(KakaoSignInRequest(accessToken))
            }

            response.andExpect {
                status { isCreated() }
            }
                .andDo {
                    documentWithHandle(
                        "auth-kakao-sign-in",
                        requestFields(
                            "accessToken" type STRING means "액세스 토큰" example accessToken
                        )
                    )
                }
        }
    }
}
