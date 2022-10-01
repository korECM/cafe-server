package zip.cafe.api.profile

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import zip.cafe.api.profile.dto.ProfileInfo
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.mockmvc.getWithPathParameter
import zip.cafe.api.utils.restdocs.*
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.entity.member.Member
import zip.cafe.service.profile.ProfileService

@WebMvcTest(ProfileController::class)
class ProfileControllerTest : WebMvcTestSpec() {
    @MockkBean
    private lateinit var profileService: ProfileService

    init {
        "유저의 프로필 조회" {
            val memberId = 5L

            every { profileService.getProfile(memberId) } returns ProfileInfo(
                id = memberId,
                nickname = "홍길동",
                profileImageURL = Member.DEFAULT_PROFILE_IMAGE_URL,
                description = "적당한 설명",
                sumOfReviewAndFootPrint = 10,
                numberOfFollowers = 151612,
                numberOfFollowees = 165,
            )

            val response = mockMvc.getWithPathParameter("/profiles/members/{memberId}", memberId)

            response.andExpect {
                status { isOk() }
            }.andDo {
                documentWithHandle(
                    "get-profile",
                    pathParameters(
                        "memberId" means "프로필을 조회하려는 유저의 Id" example "5L"
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "id" type NUMBER means "유저의 Id" example "5L",
                        "nickname" type STRING means "유저의 닉네임" example "홍길동",
                        "profileImageURL" type STRING means "유저의 프로필 이미지 URL" example "https://cafezip.s3.ap-northeast-2.amazonaws.com/profiles/5L.png",
                        "description" type STRING means "유저의 설명" example "적당한 설명",
                        "sumOfReviewAndFootPrint" type NUMBER means "유저의 리뷰와 발자국 합" example "10",
                        "numberOfFollowers" type NUMBER means "유저의 팔로워 수" example "151612",
                        "numberOfFollowees" type NUMBER means "유저의 팔로잉 수" example "165",
                    )
                )
            }
        }
    }
}
