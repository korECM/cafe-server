package zip.cafe.api

import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.print.print
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.post
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.seeds.MOCK_MVC_USER_ID
import zip.cafe.service.ReviewLikeService
import zip.cafe.service.ReviewService
import zip.cafe.utils.documentRequest
import zip.cafe.utils.documentResponse

@WebMvcTest(ReviewController::class)
class ReviewControllerTest : WebMvcTestSpec() {

    @MockkBean
    private lateinit var reviewService: ReviewService

    @MockkBean
    private lateinit var reviewLikeService: ReviewLikeService

    init {
        "리뷰 좋아요" {
            val memberId = MOCK_MVC_USER_ID
            val reviewId = 5L

            every { reviewLikeService.likeReview(memberId, reviewId) } just Runs

            val response = mockMvc.post("/reviews/$reviewId/like")

            response.print()

            response.andExpect {
                status { isCreated() }
            }
                .andDo {
                    handle(
                        MockMvcRestDocumentation.document(
                            "like-review",
                            documentRequest,
                            documentResponse,
                            responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("body").type(JsonFieldType.NULL).description("데이터"),
                            )
                        )
                    )
                }
        }

        "리뷰 좋아요 취소" {
            val memberId = MOCK_MVC_USER_ID
            val reviewId = 5L

            every { reviewLikeService.cancelLikeReview(memberId, reviewId) } just Runs

            val response = mockMvc.post("/reviews/$reviewId/unlike")

            response.print()

            response.andExpect {
                status { isNoContent() }
            }
                .andDo {
                    handle(
                        MockMvcRestDocumentation.document(
                            "unlike-review",
                            documentRequest,
                            documentResponse,
                            responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("body").type(JsonFieldType.NULL).description("데이터"),
                            )
                        )
                    )
                }
        }
    }
}
