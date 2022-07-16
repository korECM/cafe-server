package zip.cafe.api

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import zip.cafe.api.utils.mockmvc.getWithPathParameter
import zip.cafe.api.utils.restdocs.*
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.entity.review.CafeKeyword
import zip.cafe.seeds.MOCK_MVC_USER_ID
import zip.cafe.seeds.createCafe
import zip.cafe.seeds.createMenu
import zip.cafe.seeds.createReviewImage
import zip.cafe.service.CafeService
import zip.cafe.service.dto.FollowerWhoLikeCafe
import zip.cafe.service.dto.FollowerWhoWriteReview
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

            val menu1 = createMenu()
            val menu2 = createMenu()
            cafe.addMenu(menu1)
            cafe.addMenu(menu2)

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
                            "openingHours" type STRING means "카페 영업 시간" example "오전 7:00–오후 10:00",
                            "address" type STRING means "카페 주소" example "서울 강남구 삼성로104길 22 1층",
                            "reviewCount" type NUMBER means "카페에 달린 리뷰 개수" example "5",
                            "averageOfFinalScores" type NUMBER means "카페의 평균 리뷰 점수" example "4.5",
                            "keywords" type ARRAY means "사람들이 카페에 남긴 키워드 목록",
                            "keywords[].id" type NUMBER means "키워드 id" example "1L",
                            "keywords[].keyword" type STRING means "키워드 이름" example "아늑한",
                            "keywords[].emoji" type STRING means "키워드 이모지" example "🎁",
                            "cafeImages" type ARRAY means "카페 이미지",
                            "cafeImages[].id" type NUMBER means "카페 이미지 id" example "1234L",
                            "cafeImages[].url" type STRING means "이미지 주소" example "https://naver.com/logo.png",
                            "menus" type ARRAY means "카페 메뉴",
                            "menus[].id" type NUMBER means "카페 메뉴 id" example "1L",
                            "menus[].name" type STRING means "카페 메뉴 이름" example "아이스 아메리카노",
                            "menus[].price" type NUMBER means "카페 메뉴 가격" example "5000L",
                        )
                    )
                )
            }
        }

        "카페 id를 가지고 로그인한 유저의 팔로워 중 리뷰를 쓴 사람만 반환한다" {
            val cafe = createCafe(id = 5L)
            every { cafeService.findFollowerWhoWriteReview(MOCK_MVC_USER_ID, cafe.id) } returns listOf(
                FollowerWhoWriteReview(1L, "김감자"),
                FollowerWhoWriteReview(2L, "홍길동")
            )

            val response = mockMvc.getWithPathParameter("/cafes/{cafeId}/followers/write/review", cafe.id)

            response.andExpect {
                status { isOk() }
            }.andDo {
                handle(
                    document(
                        "get-cafe-followers-who-write-review",
                        pathParameters(
                            "cafeId" means "카페 id" example "5L"
                        ),
                        responseBody(
                            "body" beneathPathWithSubsectionId "body",
                            "followersWhoWriteReview" type ARRAY means "유저가 팔로우한 사람들의 리뷰 정보",
                            "followersWhoWriteReview[].id" type NUMBER means "그 사람의 id" example "1L",
                            "followersWhoWriteReview[].name" type STRING means "그 사람의 닉네임" example "홍길동",
                        )
                    )
                )
            }
        }


        "카페 id를 가지고 로그인한 유저의 팔로워 중 카페를 좋아요 한 유저만 반환한다" {
            val cafe = createCafe(id = 5L)
            every { cafeService.findFollowerWhoLikeCafe(MOCK_MVC_USER_ID, cafe.id) } returns listOf(
                FollowerWhoLikeCafe(3L, "나도현"),
                FollowerWhoLikeCafe(4L, "이진이")
            )

            val response = mockMvc.getWithPathParameter("/cafes/{cafeId}/followers/like/cafe", cafe.id)

            response.andExpect {
                status { isOk() }
            }.andDo {
                handle(
                    document(
                        "get-cafe-followers-who-like-cafe",
                        pathParameters(
                            "cafeId" means "카페 id" example "5L"
                        ),
                        responseBody(
                            "body" beneathPathWithSubsectionId "body",
                            "followersWhoLikeCafe" type ARRAY means "유저가 팔로우한 사람들의 카페 좋아요 정보",
                            "followersWhoLikeCafe[].id" type NUMBER means "그 사람의 id" example "3L",
                            "followersWhoLikeCafe[].name" type STRING means "그 사람의 닉네임" example "고길동",
                        )
                    )
                )
            }
        }
    }
}
