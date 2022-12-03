package zip.cafe.api

import io.mockk.every
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.mockmvc.getWithPathParameter
import zip.cafe.api.utils.restdocs.*
import zip.cafe.entity.cafe.CafeKeywordStat
import zip.cafe.entity.review.CafeKeyword
import zip.cafe.entity.toScore
import zip.cafe.seeds.MOCK_MVC_USER_ID
import zip.cafe.seeds.createCafe
import zip.cafe.seeds.createMenu
import zip.cafe.seeds.createReviewImage
import zip.cafe.service.dto.FollowerWhoLikeCafe
import zip.cafe.service.dto.FollowerWhoWriteReview
import zip.cafe.service.dto.ReviewSummary

class CafeControllerTest : WebMvcTestAdapter() {

    init {
        "카페 id를 가지고 카페 기본 정보를 가져온다" {
            val userId = MOCK_MVC_USER_ID
            val cafe = createCafe(id = 5L)
            val reviewSummary = listOf(
                ReviewSummary(1.0.toScore(), 3),
                ReviewSummary(2.0.toScore(), 0),
                ReviewSummary(3.0.toScore(), 30),
                ReviewSummary(4.0.toScore(), 656),
                ReviewSummary(5.0.toScore(), 123),
            )
            val cafeKeywordStats = listOf(
                CafeKeywordStat(cafe, CafeKeyword("아늑한", "🕊"), 2L),
                CafeKeywordStat(cafe, CafeKeyword( "편안한", "🤔"), 1L),
                CafeKeywordStat(cafe, CafeKeyword( "조용한", "🤫"), 5L),
            )
            val reviewImages = listOf(createReviewImage(), createReviewImage())

            val menu1 = createMenu()
            val menu2 = createMenu()
            cafe.addMenu(menu1)
            cafe.addMenu(menu2)

            every { cafeService.findByIdForDetailPage(cafe.id) } returns cafe
            every { cafeService.getReviewSummaryById(cafe.id) } returns reviewSummary
            every { cafeService.getKeywordSummaryById(cafe.id) } returns cafeKeywordStats
            every { cafeService.getFriendReviewCountByCafeId(cafe.id, userId) } returns 5L
            every { cafeService.getReviewImageSummaryById(cafe.id) } returns reviewImages

            val response = mockMvc.getWithPathParameter("/cafes/{cafeId}", cafe.id)

            response.andExpect {
                status { isOk() }
            }.andDo {
                documentWithHandle(
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
                        "location.latitude" type NUMBER means "카페 위도" example "37.508",
                        "location.longitude" type NUMBER means "카페 경도" example "127.056",
                        "countOfReviewByFollowee" type NUMBER means "팔로우한 친구들의 리뷰 개수" example "5",
                        "reviewScoreStat" type OBJECT means "리뷰 별점 통계",
                        "reviewScoreStat['1.0']" type NUMBER means "1점 리뷰 개수" example "3",
                        "reviewScoreStat['2.0']" type NUMBER means "2점 리뷰 개수" example "0",
                        "reviewScoreStat['3.0']" type NUMBER means "3점 리뷰 개수" example "30",
                        "reviewScoreStat['4.0']" type NUMBER means "4점 리뷰 개수" example "656",
                        "reviewScoreStat['5.0']" type NUMBER means "5점 리뷰 개수" example "123",
                        "keywords" type ARRAY means "사람들이 카페에 남긴 키워드 목록",
                        "keywords[].id" type NUMBER means "키워드 id" example "1L",
                        "keywords[].keyword" type STRING means "키워드 이름" example "아늑한",
                        "keywords[].emoji" type STRING means "키워드 이모지" example "🎁",
                        "keywords[].count" type NUMBER means "키워드 수" example 5L,
                        "cafeImages" type ARRAY means "카페 이미지",
                        "cafeImages[].id" type NUMBER means "카페 이미지 id" example "1234L",
                        "cafeImages[].url" type STRING means "카페 이미지 주소" example "https://naver.com/logo.png",
                        "reviewImages" type ARRAY means "리뷰 이미지",
                        "reviewImages[].id" type NUMBER means "리뷰 이미지 id" example "12L",
                        "reviewImages[].url" type STRING means "리뷰 이미지 주소" example "https://naver.com/logo.png",
                        "menus" type ARRAY means "카페 메뉴",
                        "menus[].id" type NUMBER means "카페 메뉴 id" example "1L",
                        "menus[].name" type STRING means "카페 메뉴 이름" example "아이스 아메리카노",
                        "menus[].price" type NUMBER means "카페 메뉴 가격" example "5000L"
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
                documentWithHandle(
                    "get-cafe-followers-who-write-review",
                    pathParameters(
                        "cafeId" means "카페 id" example "5L"
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "followersWhoWriteReview" type ARRAY means "유저가 팔로우한 사람들의 리뷰 정보",
                        "followersWhoWriteReview[].id" type NUMBER means "그 사람의 id" example "1L",
                        "followersWhoWriteReview[].name" type STRING means "그 사람의 닉네임" example "홍길동"
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
                documentWithHandle(
                    "get-cafe-followers-who-like-cafe",
                    pathParameters(
                        "cafeId" means "카페 id" example "5L"
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "followersWhoLikeCafe" type ARRAY means "유저가 팔로우한 사람들의 카페 좋아요 정보",
                        "followersWhoLikeCafe[].id" type NUMBER means "그 사람의 id" example "3L",
                        "followersWhoLikeCafe[].name" type STRING means "그 사람의 닉네임" example "고길동"
                    )
                )
            }
        }
    }
}
