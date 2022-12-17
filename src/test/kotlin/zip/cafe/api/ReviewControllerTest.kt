package zip.cafe.api

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.request.RequestDocumentation.partWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParts
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.post
import zip.cafe.api.dto.*
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.mockmvc.getWithPathParameter
import zip.cafe.api.utils.restdocs.*
import zip.cafe.config.formatAsDefault
import zip.cafe.connector.dto.S3FileDto
import zip.cafe.entity.Food
import zip.cafe.entity.review.Purpose
import zip.cafe.seeds.MOCK_MVC_USER_ID
import zip.cafe.seeds.createReviewImage
import java.time.LocalDate
import java.time.LocalDateTime

class ReviewControllerTest : WebMvcTestAdapter() {
    init {
        "단일 리뷰 조회" {
            val reviewId = 3L
            val memberId = MOCK_MVC_USER_ID
            val cafeId = 512L
            val reviewDetailInfo = ReviewDetailInfo(
                review = ReviewInfo(
                    id = reviewId,
                    finalScore = 4.5,
                    description = "좋은 카페이고 조용해서 좋아요",
                    visitPurpose = ReviewVisitPurposeInfo(purpose = Purpose.DATE, score = 3),
                    foods = listOf(ReviewFoodInfo(Food.BAKERY, 3), ReviewFoodInfo(Food.COFFEE, 4)),
                    images = listOf(ReviewImageInfo(1L, "https://image.com/1"), ReviewImageInfo(2L, "https://image.com/2")),
                    keywords = listOf(ReviewKeywordInfo(1L, "좋은", "🌏"), ReviewKeywordInfo(2L, "카페", "🚚")),
                    likeCount = 3,
                    isLiked = true,
                    commentCount = 2,
                    createdAt = LocalDateTime.now()
                ),
                member = ReviewMemberInfo(id = memberId, nickname = "길동길동홍길동", profileImageUrl = "https://image.com/123", isFollowed = true),
                cafe = ReviewCafeInfo(id = cafeId, name = "삼성 스타벅스", address = "송파구 어딘가 좋은 곳", cafeImage = "https://picsum.photos/200")
            )

            every { reviewService.getReview(memberId, reviewId) } returns reviewDetailInfo

            val response = mockMvc.getWithPathParameter("/reviews/{reviewId}", reviewId)

            response.andExpect {
                status { isOk() }
            }.andDo {
                documentWithHandle(
                    "get-single-review",
                    pathParameters(
                        "reviewId" means "리뷰 id" example reviewId
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "review.id" type NUMBER means "리뷰 id" example reviewId,
                        "review.finalScore" type NUMBER means "최종 점수" example reviewDetailInfo.review.finalScore,
                        "review.description" type STRING means "리뷰 내용" example reviewDetailInfo.review.description,
                        "review.visitPurpose.purpose" type STRING means "방문 목적 및 점수" example reviewDetailInfo.review.visitPurpose.purpose.toString(),
                        "review.visitPurpose.score" type NUMBER means "방문 목적 점수" example reviewDetailInfo.review.visitPurpose.score,
                        "review.foods" type ARRAY means "음식 종류 및 점수" example reviewDetailInfo.review.foods.toString(),
                        "review.foods[].food" type STRING means "음식 종류" example reviewDetailInfo.review.foods[0].food.toString(),
                        "review.foods[].score" type NUMBER means "음식 점수" example reviewDetailInfo.review.foods[0].score,
                        "review.images" type ARRAY means "리뷰에 올라온 이미지 목록",
                        "review.images[].id" type NUMBER means "리뷰 이미지 id" example reviewDetailInfo.review.images[0].id,
                        "review.images[].url" type STRING means "리뷰 이미지 url" example reviewDetailInfo.review.images[0].url,
                        "review.keywords" type ARRAY means "리뷰에 올라온 키워드 목록",
                        "review.keywords[].id" type NUMBER means "키워드 id" example "5L",
                        "review.keywords[].keyword" type STRING means "키워드 이름" example "친절한",
                        "review.keywords[].emoji" type STRING means "키워드 이모지" example "✨",
                        "review.isLiked" type BOOLEAN means "좋아요 여부" example reviewDetailInfo.review.isLiked,
                        "review.likeCount" type NUMBER means "좋아요 수" example reviewDetailInfo.review.likeCount,
                        "review.commentCount" type NUMBER means "댓글 수" example reviewDetailInfo.review.commentCount,
                        "review.createdAt" type STRING means "리뷰 작성 시간" example reviewDetailInfo.review.createdAt.formatAsDefault(),
                        "member.id" type NUMBER means "작성자 id" example reviewDetailInfo.member.id,
                        "member.nickname" type STRING means "작성자 닉네임" example reviewDetailInfo.member.nickname,
                        "member.profileImageUrl" type STRING means "작성자 프로필 이미지 url" example reviewDetailInfo.member.profileImageUrl,
                        "member.isFollowed" type BOOLEAN means "팔로우 여부" example false,
                        "cafe.id" type NUMBER means "카페 id" example reviewDetailInfo.cafe.id,
                        "cafe.name" type STRING means "카페 이름" example reviewDetailInfo.cafe.name,
                        "cafe.address" type STRING means "카페 주소" example reviewDetailInfo.cafe.address,
                        "cafe.cafeImage" type STRING means "카페 대표 이미지 url" example reviewDetailInfo.cafe.cafeImage
                    )
                )
            }
        }

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
                        "finalScore" type NUMBER means "최종 리뷰 점수" example "4"
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
                            "url" type STRING means "이미지 주소" example "https://techblog.woowahan.com/wp-content/uploads/img/2020-05-13/rest-docs-09.png"
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
