package zip.cafe.api

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import zip.cafe.api.utils.restdocs.document
import zip.cafe.api.utils.restdocs.means
import zip.cafe.api.utils.restdocs.requestParameters
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

            val response = mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/kakao/signIn").param("accessToken", accessToken))

            response.andExpect(
                MockMvcResultMatchers.status().isCreated
            ).andDo(
                document(
                    "auth-kakao-sign-in",
                    requestParameters(
                        "accessToken" means "액세스 토큰" example accessToken
                    )
                )
            )
        }
    }
}
