package zip.cafe.api.auth

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import zip.cafe.api.auth.dto.AppleSignInRequest
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.restdocs.STRING
import zip.cafe.api.utils.restdocs.requestFields
import zip.cafe.api.utils.restdocs.type
import zip.cafe.api.utils.spec.WebMvcTestSpec

@WebMvcTest(AppleAuthController::class)
class AppleAuthControllerTest : WebMvcTestSpec() {
    init {
        "애플 로그인 / 회원가입" {
            val identityToken = "JWjHXiVIlkxciAy_fTiEft3wDaAdHvOVcV_D6wwpCinI2gAAAYMNQf1c"

            val response = mockMvc.post("/auth/apple/signIn") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(AppleSignInRequest(identityToken))
            }

            response.andExpect {
                status { isCreated() }
            }
                .andDo {
                    documentWithHandle(
                        "auth-apple-sign-in",
                        requestFields(
                            "identityToken" type STRING means "Identity 토큰" example identityToken
                        )
                    )
                }
        }
    }
}
