package zip.cafe.api.auth

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import zip.cafe.api.auth.dto.AppleSignInRequest
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.restdocs.STRING
import zip.cafe.api.utils.restdocs.requestFields
import zip.cafe.api.utils.restdocs.responseBody
import zip.cafe.api.utils.restdocs.type
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.seeds.MOCK_MVC_USER_ID
import zip.cafe.service.auth.AppleAuthService
import zip.cafe.service.auth.AuthService

@WebMvcTest(AppleAuthController::class)
class AppleAuthControllerTest : WebMvcTestSpec() {

    @MockkBean
    private lateinit var appleAuthService: AppleAuthService

    @MockkBean
    private lateinit var authService: AuthService

    init {
        "애플 로그인 / 회원가입" {
            val identityToken = "JWjHXiVIlkxciAy_fTiEft3wDaAdHvOVcV_D6wwpCinI2gAAAYMNQf1c"
            val firstName = "길동"
            val lastName = "홍"
            val memberId = MOCK_MVC_USER_ID
            val jwtToken =
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1IiwiaWF0IjoxNjU4OTI4Mjg1LCJleHAiOjE2NTg5NTk4MjF9.oAc8ycJgnmM5crByz0DrvKEoH3xGceqAWPHLFtUIwTHFQopf9kbSYXsR5FML05_FUbdPIf_FGKPwo_bdIjgOyw"

            every { appleAuthService.findMemberIdByAppleIdentityToken(identityToken, lastName + firstName) } returns memberId
            every { authService.generateToken(memberId, any()) } returns jwtToken


            val response = mockMvc.post("/auth/apple/signIn") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(AppleSignInRequest(identityToken, firstName, lastName))
            }

            response.andExpect {
                status { isCreated() }
            }
                .andDo {
                    documentWithHandle(
                        "auth-apple-sign-in",
                        requestFields(
                            "identityToken" type STRING means "Identity 토큰" example identityToken,
                            "firstName" type STRING means "firstName" example firstName,
                            "lastName" type STRING means "lastName" example lastName,
                        ),
                        responseBody(
                            "token" type STRING means "JWT 토큰" example jwtToken
                        )
                    )
                }
        }
    }
}
