package zip.cafe.api

import io.mockk.every
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.post
import zip.cafe.api.dto.FootPrintRegisterRequest
import zip.cafe.api.dto.ReviewRegisterFromFootprintRequest
import zip.cafe.api.dto.ReviewRegisterFromFootprintRequest.FoodInfo
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.mockmvc.postWithPathParameter
import zip.cafe.api.utils.restdocs.*
import zip.cafe.config.formatAsDefault
import zip.cafe.entity.Food
import zip.cafe.entity.review.Purpose
import zip.cafe.seeds.MOCK_MVC_USER_ID
import java.time.LocalDate

class FootPrintControllerTest : WebMvcTestAdapter() {

    init {
        "발자국 등록" {
            val cafeId = 5L
            val userId = MOCK_MVC_USER_ID
            val visitDate = LocalDate.now()

            val footprintId = 5L
            val request = FootPrintRegisterRequest(cafeId, visitDate)

            every { reviewService.createFootprint(cafeId, userId, visitDate) } returns footprintId

            val response = mockMvc.post("/footprints") {
                this.contentType = APPLICATION_JSON
                this.content = objectMapper.writeValueAsString(request)
            }

            response.andExpect {
                status { isCreated() }
            }.andDo {
                documentWithHandle(
                    "write-footprint",
                    requestFields(
                        "cafeId" type NUMBER means "카페 Id" example cafeId.toString(),
                        "visitDate" type DATE means "방문 날짜" example visitDate.formatAsDefault()
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "footprintId" type NUMBER means "생성된 발자국 Id 토큰" example footprintId
                    )
                )
            }
        }

        "발자국으로부터 리뷰 작성" {
            val footprintId = 30L
            val memberId = MOCK_MVC_USER_ID

            val visitPurpose = Purpose.STUDY
            val visitPurposeScore = 5

            val food1 = Food.BAKERY
            val food2 = Food.BEVERAGE
            val foodScore1 = 4
            val foodScore2 = 3

            val keywords = listOf(1L, 2L)
            val reviewImageIds = listOf(3L, 5L, 6L)

            val description = "좋은 카페"

            val finalScore = 3.0

            val reviewId = 5L

            val request = ReviewRegisterFromFootprintRequest(
                visitPurpose = visitPurpose,
                visitPurposeScore = visitPurposeScore,
                foodInfos = listOf(FoodInfo(food1, foodScore1), FoodInfo(food2, foodScore2)),
                keywords = keywords,
                reviewImageIds = reviewImageIds,
                description = description,
                finalScore = finalScore
            )

            every { reviewService.createReview(footprintId, memberId, request.toDto()) } returns reviewId

            val response = mockMvc.postWithPathParameter("/footprints/{footprintId}/reviews", footprintId) {
                this.contentType = APPLICATION_JSON
                this.content = objectMapper.writeValueAsString(request)
            }

            response.andExpect {
                status { isCreated() }
            }.andDo {
                documentWithHandle(
                    "write-review-from-footprint",
                    pathParameters(
                        "footprintId" means "발자국 Id" example footprintId
                    ),
                    requestFields(
                        "visitPurpose" type ENUM(Purpose::class) means "방문 목적",
                        "visitPurposeScore" type NUMBER means "방문 목적 점수" example "3",
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
    }
}
