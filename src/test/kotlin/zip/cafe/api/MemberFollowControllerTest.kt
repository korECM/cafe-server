package zip.cafe.api

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import zip.cafe.api.utils.mockmvc.postWithPathParameter
import zip.cafe.api.utils.restdocs.document
import zip.cafe.api.utils.restdocs.means
import zip.cafe.api.utils.restdocs.pathParameters
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.seeds.MOCK_MVC_USER_ID
import zip.cafe.service.MemberFollowService

@WebMvcTest(MemberFollowController::class)
class MemberFollowControllerTest : WebMvcTestSpec() {

    @MockkBean
    private lateinit var memberFollowService: MemberFollowService

    init {
        "유저를 팔로우한다" {
            val fromMemberId = MOCK_MVC_USER_ID
            val targetMemberId = 5L

            every { memberFollowService.follow(fromMemberId, targetMemberId) } just Runs

            val response = mockMvc.postWithPathParameter("/members/{targetMemberId}/follow", targetMemberId)

            response.andExpect {
                status { isCreated() }
            }.andDo {
                handle(
                    document(
                        "follow",
                        pathParameters(
                            "targetMemberId" means "팔로우하려는 대상의 id" example "5L"
                        )
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
                handle(
                    document(
                        "unfollow",
                        pathParameters(
                            "targetMemberId" means "언팔로우하려는 대상의 id" example "3L"
                        )
                    )
                )
            }
        }
    }
}
