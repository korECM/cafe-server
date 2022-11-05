package zip.cafe.api.auth

import io.mockk.every
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import zip.cafe.api.WebMvcTestAdapter
import zip.cafe.api.auth.dto.KakaoSignInRequest
import zip.cafe.api.profile.dto.CheckProfileResult
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.restdocs.*
import zip.cafe.seeds.MOCK_MVC_USER_ID

class KakaoAuthControllerTest : WebMvcTestAdapter() {
    init {
        "카카오 로그인 / 회원가입" {
            val memberId = MOCK_MVC_USER_ID
            val accessToken = "JWjHXiVIlkxciAy_fTiEft3wDaAdHvOVcV_D6wwpCinI2gAAAYMNQf1c"
            val jwtToken =
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1IiwiaWF0IjoxNjU4OTI4Mjg1LCJleHAiOjE2NTg5NTk4MjF9.oAc8ycJgnmM5crByz0DrvKEoH3xGceqAWPHLFtUIwTHFQopf9kbSYXsR5FML05_FUbdPIf_FGKPwo_bdIjgOyw"

            every { kakaoAuthService.findMemberIdByKakaoAccessToken(accessToken) } returns memberId
            every { authService.generateToken(memberId, any()) } returns jwtToken
            every { profileService.checkProfileInit(memberId) } returns CheckProfileResult(
                memberId = memberId,
                isInit = true,
                nickname = "nickname",
                profileImageURL = "profileImageURL"
            )

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
                        ),
                        responseBody(
                            "body" beneathPathWithSubsectionId "body",
                            "token" type STRING means "JWT 토큰" example jwtToken,
                            "isProfileInit" type BOOLEAN means "프로필 초기화 여부" example false,
                            "nickname" type STRING means "닉네임" example "홍길동",
                            "profileImageURL" type STRING means "" example "https://www.google.com"
                        )
                    )
                }
        }
    }
}
