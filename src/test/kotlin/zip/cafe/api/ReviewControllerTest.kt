package zip.cafe.api

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.partWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParts
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.post
import zip.cafe.api.utils.restdocs.*
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.connector.dto.S3FileDto
import zip.cafe.seeds.MOCK_MVC_USER_ID
import zip.cafe.seeds.createReviewImage
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

        "이미지 업로드" {
            val memberId = MOCK_MVC_USER_ID
            val bucket = "bucket"
            val fileName1 = "file1"
            val fileName2 = "file2"

            val reviewImageId1 = 3L
            val reviewImageId2 = 5L

            val files = listOf(MockMultipartFile(fileName1, "testData".toByteArray()), MockMultipartFile(fileName2, "anotherData".toByteArray()))
            val cloudFrontURL1 = "url2"
            val cloudFrontURL2 = "url5"
            val uploadedFiles = listOf(S3FileDto(bucket, fileName1, "url1", cloudFrontURL1), S3FileDto(bucket, fileName2, "url3", cloudFrontURL2))

            val reviewImages = listOf(
                createReviewImage(id = reviewImageId1, cloudFrontURL = cloudFrontURL1),
                createReviewImage(id = reviewImageId2, cloudFrontURL = cloudFrontURL2)
            )

            every { reviewService.uploadReviewImages(any()) } returns uploadedFiles
            every { reviewService.saveUploadedReviewImage(memberId, uploadedFiles) } returns reviewImages

            val response = mockMvc.multipart("/reviews/images") {
                files.forEach { file("images", it.bytes) }
            }

            response.andExpect {
                status { isCreated() }
            }
                .andDo {
                    handle(
                        MockMvcRestDocumentation.document(
                            "upload-review-images",
                            documentRequest,
                            documentResponse,
                            requestParts(
                                partWithName("images").description("업로드 할 리뷰 이미지 파일 목록")
                            ),
                            responseBody(
                                "message" type STRING means "응답 메시지",
                                "body" type ARRAY means "데이터",
                                "body[].id" type NUMBER means "이미지 id" example "3",
                                "body[].url" type STRING means "이미지 주소" example "https://techblog.woowahan.com/wp-content/uploads/img/2020-05-13/rest-docs-09.png",
                            )
                        )
                    )
                }
        }

        "리뷰 좋아요" {
            val memberId = MOCK_MVC_USER_ID
            val reviewId = 5L

            every { reviewLikeService.likeReview(memberId, reviewId) } just Runs

            val response = mockMvc.post("/reviews/$reviewId/like")

            response.andExpect {
                status { isCreated() }
            }
                .andDo {
                    handle(
                        MockMvcRestDocumentation.document(
                            "like-review",
                            documentRequest,
                            documentResponse,
                            responseBody(
                                "message" type STRING means "응답 메시지",
                                "body" type NULL means "데이터",
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

            response.andExpect {
                status { isNoContent() }
            }
                .andDo {
                    handle(
                        MockMvcRestDocumentation.document(
                            "unlike-review",
                            documentRequest,
                            documentResponse,
                            responseBody(
                                "message" type STRING means "응답 메시지",
                                "body" type NULL means "데이터",
                            )
                        )
                    )
                }
        }
    }
}
