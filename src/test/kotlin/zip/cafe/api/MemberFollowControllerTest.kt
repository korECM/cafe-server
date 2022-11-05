package zip.cafe.api

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.mockmvc.postWithPathParameter
import zip.cafe.api.utils.restdocs.means
import zip.cafe.api.utils.restdocs.pathParameters
import zip.cafe.seeds.MOCK_MVC_USER_ID

class MemberFollowControllerTest : WebMvcTestAdapter() {
    init {
        "유저를 팔로우한다" {
            val fromMemberId = MOCK_MVC_USER_ID
            val targetMemberId = 5L

            every { memberFollowService.follow(fromMemberId, targetMemberId) } just Runs

            val response = mockMvc.postWithPathParameter("/members/{targetMemberId}/follow", targetMemberId)

            response.andExpect {
                status { isCreated() }
            }.andDo {
                documentWithHandle(
                    "follow",
                    pathParameters(
                        "targetMemberId" means "팔로우하려는 대상의 id" example "5L"
                    )
                )
            }
        }

        "유저를 언팔로우한다" {
            val fromMemberId = MOCK_MVC_USER_ID
            val targetMemberId = 3L

            every { memberFollowService.unfollow(fromMemberId, targetMemberId) } just Runs

            val response = mockMvc.postWithPathParameter("/members/{targetMemberId}/unfollow", targetMemberId)

            response.andExpect {
                status { isAccepted() }
            }.andDo {
                documentWithHandle(
                    "unfollow",
                    pathParameters(
                        "targetMemberId" means "언팔로우하려는 대상의 id" example "3L"
                    )
                )
            }
        }
    }
}
