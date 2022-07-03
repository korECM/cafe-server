package zip.cafe.api

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import zip.cafe.api.utils.mockmvc.getWithPathParameter
import zip.cafe.api.utils.restdocs.*
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.entity.review.CafeKeyword
import zip.cafe.seeds.createCafe
import zip.cafe.seeds.createReviewImage
import zip.cafe.service.CafeService
import zip.cafe.service.dto.ReviewSummary

@WebMvcTest(CafeController::class)
class CafeControllerTest : WebMvcTestSpec() {

    @MockkBean
    private lateinit var cafeService: CafeService

    init {
        "카페 id를 가지고 카페 기본 정보를 가져온다" {
            val cafe = createCafe(id = 5L)
            val reviewSummary = ReviewSummary(5L, 2.5)
            val cafeKeywords = listOf(CafeKeyword("아늑한", "🕊"), CafeKeyword("편안한", "🤔"))
            val reviewImages = listOf(createReviewImage(), createReviewImage())

            every { cafeService.findByIdForDetailPage(cafe.id) } returns cafe
            every { cafeService.getReviewSummaryById(cafe.id) } returns reviewSummary
            every { cafeService.getKeywordSummaryById(cafe.id) } returns cafeKeywords
            every { cafeService.getImageSummaryById(cafe.id) } returns reviewImages

            val response = mockMvc.getWithPathParameter("/cafes/{cafeId}", cafe.id)

            response.andExpect {
                status { isOk() }
            }.andDo {
                handle(
                    document(
                        "get-cafe",
                        pathParameters(
                            "cafeId" means "카페 id" example "5L"
                        ),
                        responseBody(
                            "body" beneathPathWithSubsectionId "body",
                            "id" type NUMBER means "카페 id" example "5L",
                            "name" type STRING means "카페 이름" example "북앤레스트",
                            "address" type STRING means "카페 주소" example "서울 강남구 삼성로104길 22 1층",
                            "reviewCount" type NUMBER means "카페에 달린 리뷰 개수" example "5",
                            "averageOfFinalScores" type NUMBER means "카페의 평균 리뷰 점수" example "4.5",
                            "keywords" type ARRAY means "사람들이 카페에 남긴 키워드 목록",
                            "keywords[].id" type NUMBER means "키워드 id" example "1L",
                            "keywords[].keyword" type STRING means "키워드 이름" example "아늑한",
                            "keywords[].emoji" type STRING means "키워드 이모지" example "🎁",
                            "cafeImages" type ARRAY means "카페 이미지",
                            "cafeImages[].id" type NUMBER means "카페 이미지 id" example "1234L",
                            "cafeImages[].url" type STRING means "이미지 주소" example "https://naver.com/logo.png"
                        )
                    )
                )
            }
        }
    }
}
