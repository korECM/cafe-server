package zip.cafe.api

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.request.RequestDocumentation.partWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParts
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.post
import zip.cafe.api.dto.ReviewRegisterRequest
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.restdocs.*
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.config.formatAsDefault
import zip.cafe.connector.dto.S3FileDto
import zip.cafe.entity.Food
import zip.cafe.entity.review.Purpose
import zip.cafe.seeds.MOCK_MVC_USER_ID
import zip.cafe.seeds.createReviewImage
import zip.cafe.service.ReviewLikeService
import zip.cafe.service.ReviewService
import java.time.LocalDate

@WebMvcTest(ReviewController::class)
class ReviewControllerTest : WebMvcTestSpec() {

    @MockkBean
    private lateinit var reviewService: ReviewService

    @MockkBean
    private lateinit var reviewLikeService: ReviewLikeService

    init {

        "리뷰 업로드" {
            val cafeId = 1L

            val visitPurpose = Purpose.STUDY
            val visitPurposeScore = 5
            val visitDate = LocalDate.now()

            val food1 = Food.BAKERY
            val food2 = Food.BEVERAGE
            val foodScore1 = 4
            val foodScore2 = 3

            val keywords = listOf(1L, 2L)
            val reviewImageIds = listOf(3L, 5L, 6L)

            val description = "좋은 카페"

            val finalScore = 3.0

            val reviewId = 5L

            val request = ReviewRegisterRequest(
                cafeId = cafeId,
                visitPurpose = visitPurpose,
                visitPurposeScore = visitPurposeScore,
                foodInfos = listOf(ReviewRegisterRequest.FoodInfo(food1, foodScore1), ReviewRegisterRequest.FoodInfo(food2, foodScore2)),
                keywords = keywords,
                reviewImageIds = reviewImageIds,
                description = description,
                finalScore = finalScore,
                visitDate = visitDate
            )

            every { reviewService.createFootprintAndReview(cafeId, MOCK_MVC_USER_ID, visitDate, request.toDto()) } returns reviewId

            val response = mockMvc.post("/reviews") {
                this.contentType = APPLICATION_JSON
                this.content = objectMapper.writeValueAsString(request)
            }

            response.andExpect {
                status { isCreated() }
            }.andDo {
                documentWithHandle(
                    "write-review",
                    requestFields(
                        "cafeId" type NUMBER means "카페 Id" example "1L",
                        "visitPurpose" type ENUM(Purpose::class) means "방문 목적",
                        "visitPurposeScore" type NUMBER means "방문 목적 점수" example "3",
                        "visitDate" type DATE means "방문 날짜" example visitDate.formatAsDefault(),
                        "foodInfos" type ARRAY means "카페에서 먹은 음식 정보",
                        "foodInfos[].food" type ENUM(Food::class) means "먹은 음식",
                        "foodInfos[].score" type NUMBER means "먹은 음식에 대한 점수" example "3.0",
                        "keywords" type ARRAY means "키워드 API에서 내려준 키워드 Id 리스트" example "[1, 2]",
                        "reviewImageIds" type ARRAY means "업로드한 리뷰 이미지 Id 리스트" example "[3, 5, 6]",
                        "description" type STRING means "리뷰 내용" example "커피도 맛있고 친절한 카페",
                        "finalScore" type NUMBER means "최종 리뷰 점수" example "4",
                    )
                )
            }
        }

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
                    documentWithHandle(
                        "upload-review-images",
                        requestParts(
                            partWithName("images").description("업로드 할 리뷰 이미지 파일 목록")
                        ),
                        responseBody(
                            "body" beneathPathWithSubsectionId "body",
                            "id" type NUMBER means "이미지 id" example "3",
                            "url" type STRING means "이미지 주소" example "https://techblog.woowahan.com/wp-content/uploads/img/2020-05-13/rest-docs-09.png",
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
                    documentWithHandle(
                        "like-review"
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
                    documentWithHandle(
                        "unlike-review"
                    )
                }
        }
    }
}
