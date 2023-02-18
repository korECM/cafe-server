package zip.cafe.api

import io.mockk.every
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import zip.cafe.api.dto.ReviewForCafeInfo
import zip.cafe.api.dto.ReviewWithPagination
import zip.cafe.api.dto.ReviewWithoutPagination
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.mockmvc.getWithPathParameter
import zip.cafe.api.utils.restdocs.*
import zip.cafe.entity.cafe.CafeKeywordStat
import zip.cafe.entity.review.CafeKeyword
import zip.cafe.entity.toScore
import zip.cafe.seeds.*
import zip.cafe.service.dto.FollowerWhoLikeCafe
import zip.cafe.service.dto.FollowerWhoWriteReview
import zip.cafe.service.dto.ReviewSummary
import java.time.LocalDateTime

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
                CafeKeywordStat(cafe, CafeKeyword("편안한", "🤔"), 1L),
                CafeKeywordStat(cafe, CafeKeyword("조용한", "🤫"), 5L),
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

        "카페의 id를 가지고 그 카페의 리뷰를 반환한다" {
            val userId = MOCK_MVC_USER_ID
            val cafe = createCafe(id = 5L)
            val minReviewId = 7L
            val limit = 2L
            every {
                cafeService.getDetailReviewsByCafeIdAndUserId(cafe.id, userId, minReviewId, limit)
            } returns ReviewWithPagination(
                reviews = listOf(
                    ReviewForCafeInfo(
                        id = 5L,
                        member = ReviewForCafeInfo.ReviewMemberInfo(createMember()),
                        review = ReviewForCafeInfo.ReviewInfo(
                            id = 1L,
                            finalScore = 4.0,
                            images = listOf(
                                ReviewForCafeInfo.ReviewImageInfo(1L, "https://picsum.photos/200"),
                                ReviewForCafeInfo.ReviewImageInfo(2L, "https://picsum.photos/200")
                            ),
                            keywords = listOf(
                                ReviewForCafeInfo.ReviewKeywordInfo(1L, "조용한", "😵‍💫"),
                                ReviewForCafeInfo.ReviewKeywordInfo(2L, "아늑한", "😯"),
                            ),
                            likeCount = 5,
                            description = "조용하고 좋은 카페",
                            commentCount = 3,
                            isLiked = true,
                            isFolloweeReview = false,
                            createdAt = LocalDateTime.now().minusDays(3)
                        )
                    ),
                    ReviewForCafeInfo(
                        id = 7L,
                        member = ReviewForCafeInfo.ReviewMemberInfo(createMember()),
                        review = ReviewForCafeInfo.ReviewInfo(
                            id = 2L,
                            finalScore = 3.0,
                            images = listOf(
                                ReviewForCafeInfo.ReviewImageInfo(3L, "https://picsum.photos/200"),
                                ReviewForCafeInfo.ReviewImageInfo(4L, "https://picsum.photos/200")
                            ),
                            keywords = listOf(
                                ReviewForCafeInfo.ReviewKeywordInfo(1L, "조용한", "😵‍💫"),
                                ReviewForCafeInfo.ReviewKeywordInfo(3L, "뷰가 좋은", "👻"),
                            ),
                            likeCount = 2,
                            description = "사진 찍기 좋아요",
                            commentCount = 0,
                            isLiked = false,
                            isFolloweeReview = true,
                            createdAt = LocalDateTime.now().minusDays(5).minusHours(3)
                        )
                    )
                ),
                isLastPage = true
            )

            val response = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/cafes/{cafeId}/reviews", cafe.id)
                    .param("minReviewId", minReviewId.toString())
                    .param("limit", limit.toString())
            )

            response.andExpect(
                MockMvcResultMatchers.status().isOk
            ).andDo(
                document(
                    "get-cafe-reviews",
                    pathParameters(
                        "cafeId" means "카페 id" example "5L",
                        "minReviewId" means "조회한 리뷰 목록 중 가장 작은 id 값" isOptional true example "5L",
                        "limit" means "한번에 조회하려는 리뷰 개수" isOptional true default "10L" example "10L"
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "isLastPage" type BOOLEAN means "마지막 페이지 여부" example "true",
                        "reviews" type ARRAY means "리뷰 목록",
                        "reviews[].id" type NUMBER means "리뷰 id" example "5L",
                        "reviews[].member" type OBJECT means "리뷰 작성자 정보",
                        "reviews[].member.id" type NUMBER means "리뷰 작성자 id" example "1L",
                        "reviews[].member.name" type STRING means "리뷰 작성자 닉네임" example "고길동",
                        "reviews[].member.profileImage" type STRING means "리뷰 작성자 프로필 이미지" example "https://picsum.photos/200",
                        "reviews[].review" type OBJECT means "리뷰 정보",
                        "reviews[].review.id" type NUMBER means "리뷰 id" example "1L",
                        "reviews[].review.finalScore" type NUMBER means "리뷰 최종 점수" example "4.0",
                        "reviews[].review.images" type ARRAY means "리뷰 이미지 목록",
                        "reviews[].review.images[].id" type NUMBER means "리뷰 이미지 id" example "1L",
                        "reviews[].review.images[].url" type STRING means "리뷰 이미지 url" example "https://picsum.photos/200",
                        "reviews[].review.keywords" type ARRAY means "리뷰 키워드 목록",
                        "reviews[].review.keywords[].id" type NUMBER means "리뷰 키워드 id" example "1L",
                        "reviews[].review.keywords[].keyword" type STRING means "리뷰 키워드 이름" example "조용한",
                        "reviews[].review.keywords[].emoji" type STRING means "리뷰 키워드 이모지" example "😵‍💫",
                        "reviews[].review.isLiked" type BOOLEAN means "리뷰 좋아요 여부" example "true",
                        "reviews[].review.isFolloweeReview" type BOOLEAN means "리뷰 작성자 팔로우 여부" example "false",
                        "reviews[].review.likeCount" type NUMBER means "리뷰 좋아요 개수" example "5",
                        "reviews[].review.description" type STRING means "리뷰 내용" example "조용하고 좋은 카페",
                        "reviews[].review.commentCount" type NUMBER means "리뷰에 달린 댓글 수",
                        "reviews[].review.createdAt" type STRING means "리뷰 작성 시간" formattedAs "yyyy-MM-dd HH:mm:ss" example "2021-08-01T00:00:00",
                    )
                )
            )
        }

        "카페의 id를 가지고 그 카페의 팔로워 리뷰를 반환한다" {
            val userId = MOCK_MVC_USER_ID
            val cafe = createCafe(id = 5L)
            every {
                cafeService.getFollowerReviewByCafeIdAndUserId(cafe.id, userId)
            } returns ReviewWithoutPagination(
                reviews = listOf(
                    ReviewForCafeInfo(
                        id = 5L,
                        member = ReviewForCafeInfo.ReviewMemberInfo(createMember()),
                        review = ReviewForCafeInfo.ReviewInfo(
                            id = 1L,
                            finalScore = 4.0,
                            images = listOf(
                                ReviewForCafeInfo.ReviewImageInfo(1L, "https://picsum.photos/200"),
                                ReviewForCafeInfo.ReviewImageInfo(2L, "https://picsum.photos/200")
                            ),
                            keywords = listOf(
                                ReviewForCafeInfo.ReviewKeywordInfo(1L, "조용한", "😵‍💫"),
                                ReviewForCafeInfo.ReviewKeywordInfo(2L, "아늑한", "😯"),
                            ),
                            likeCount = 5,
                            description = "조용하고 좋은 카페",
                            commentCount = 3,
                            isLiked = true,
                            isFolloweeReview = false,
                            createdAt = LocalDateTime.now().minusDays(3)
                        )
                    ),
                    ReviewForCafeInfo(
                        id = 7L,
                        member = ReviewForCafeInfo.ReviewMemberInfo(createMember()),
                        review = ReviewForCafeInfo.ReviewInfo(
                            id = 2L,
                            finalScore = 3.0,
                            images = listOf(
                                ReviewForCafeInfo.ReviewImageInfo(3L, "https://picsum.photos/200"),
                                ReviewForCafeInfo.ReviewImageInfo(4L, "https://picsum.photos/200")
                            ),
                            keywords = listOf(
                                ReviewForCafeInfo.ReviewKeywordInfo(1L, "조용한", "😵‍💫"),
                                ReviewForCafeInfo.ReviewKeywordInfo(3L, "뷰가 좋은", "👻"),
                            ),
                            likeCount = 2,
                            description = "사진 찍기 좋아요",
                            commentCount = 0,
                            isLiked = false,
                            isFolloweeReview = true,
                            createdAt = LocalDateTime.now().minusDays(5).minusHours(3)
                        )
                    )
                )
            )

            val response = mockMvc.perform(RestDocumentationRequestBuilders.get("/cafes/{cafeId}/reviews/follower", cafe.id))

            response.andExpect(
                MockMvcResultMatchers.status().isOk
            ).andDo(
                document(
                    "get-cafe-reviews-follower",
                    pathParameters(
                        "cafeId" means "카페 id" example "5L",
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "reviews" type ARRAY means "리뷰 목록",
                        "reviews[].id" type NUMBER means "리뷰 id" example "5L",
                        "reviews[].member" type OBJECT means "리뷰 작성자 정보",
                        "reviews[].member.id" type NUMBER means "리뷰 작성자 id" example "1L",
                        "reviews[].member.name" type STRING means "리뷰 작성자 닉네임" example "고길동",
                        "reviews[].member.profileImage" type STRING means "리뷰 작성자 프로필 이미지" example "https://picsum.photos/200",
                        "reviews[].review" type OBJECT means "리뷰 정보",
                        "reviews[].review.id" type NUMBER means "리뷰 id" example "1L",
                        "reviews[].review.finalScore" type NUMBER means "리뷰 최종 점수" example "4.0",
                        "reviews[].review.images" type ARRAY means "리뷰 이미지 목록",
                        "reviews[].review.images[].id" type NUMBER means "리뷰 이미지 id" example "1L",
                        "reviews[].review.images[].url" type STRING means "리뷰 이미지 url" example "https://picsum.photos/200",
                        "reviews[].review.keywords" type ARRAY means "리뷰 키워드 목록",
                        "reviews[].review.keywords[].id" type NUMBER means "리뷰 키워드 id" example "1L",
                        "reviews[].review.keywords[].keyword" type STRING means "리뷰 키워드 이름" example "조용한",
                        "reviews[].review.keywords[].emoji" type STRING means "리뷰 키워드 이모지" example "😵‍💫",
                        "reviews[].review.isLiked" type BOOLEAN means "리뷰 좋아요 여부" example "true",
                        "reviews[].review.isFolloweeReview" type BOOLEAN means "리뷰 작성자 팔로우 여부" example "true",
                        "reviews[].review.likeCount" type NUMBER means "리뷰 좋아요 개수" example "5",
                        "reviews[].review.description" type STRING means "리뷰 내용" example "조용하고 좋은 카페",
                        "reviews[].review.commentCount" type NUMBER means "리뷰에 달린 댓글 수",
                        "reviews[].review.createdAt" type STRING means "리뷰 작성 시간" formattedAs "yyyy-MM-dd HH:mm:ss" example "2021-08-01T00:00:00",
                    )
                )
            )
        }
    }
}
