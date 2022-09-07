package zip.cafe.api.auth

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import zip.cafe.api.auth.dto.KakaoSignInRequest
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.restdocs.STRING
import zip.cafe.api.utils.restdocs.requestFields
import zip.cafe.api.utils.restdocs.type
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.seeds.MOCK_MVC_USER_ID
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
            val memberId = MOCK_MVC_USER_ID
            val accessToken = "JWjHXiVIlkxciAy_fTiEft3wDaAdHvOVcV_D6wwpCinI2gAAAYMNQf1c"
            val jwtToken =
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1IiwiaWF0IjoxNjU4OTI4Mjg1LCJleHAiOjE2NTg5NTk4MjF9.oAc8ycJgnmM5crByz0DrvKEoH3xGceqAWPHLFtUIwTHFQopf9kbSYXsR5FML05_FUbdPIf_FGKPwo_bdIjgOyw"

            every { kakaoAuthService.findMemberIdByKakaoAccessToken(accessToken) } returns memberId
            every { authService.generateToken(memberId, any()) } returns jwtToken

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
