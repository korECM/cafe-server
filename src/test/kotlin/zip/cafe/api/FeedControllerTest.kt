package zip.cafe.api

import io.mockk.every
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import zip.cafe.api.dto.*
import zip.cafe.api.utils.restdocs.*
import zip.cafe.seeds.*

class FeedControllerTest : WebMvcTestAdapter() {

    init {
        "팔로워들의 최신 리뷰들을 가져온다" {
            val review1 = createReview(id = 1L)

            val reviewImage1Of1 = createReviewImageWithReview()
            val reviewImage1Of2 = createReviewImageWithReview()
            review1.addImage(reviewImage1Of1)
            review1.addImage(reviewImage1Of2)

            val review2 = createReview(id = 2L)
            val reviewImage2Of1 = createReviewImageWithReview()
            review2.addImage(reviewImage2Of1)
            val review3 = createReview(id = 3L)
            val reviewImage3Of1 = createReviewImageWithReview()
            review3.addImage(reviewImage3Of1)
            val review4 = createReview(id = 4L)
            val reviewImage4Of1 = createReviewImageWithReview()
            review4.addImage(reviewImage4Of1)
            val review5 = createReview(id = 5L)
            val reviewImage5Of1 = createReviewImageWithReview()
            review5.addImage(reviewImage5Of1)
            val review6 = createReview(id = 6L)
            val reviewImage6Of1 = createReviewImageWithReview()
            review6.addImage(reviewImage6Of1)
            val review7 = createReview(id = 7L)
            val reviewImage7Of1 = createReviewImageWithReview()
            review7.addImage(reviewImage7Of1)
            val review8 = createReview(id = 8L)
            val reviewImage8Of1 = createReviewImageWithReview()
            review8.addImage(reviewImage8Of1)
            val review9 = createReview(id = 9L)
            val reviewImage9Of1 = createReviewImageWithReview()
            review9.addImage(reviewImage9Of1)
            review9.addCafeKeyword(createCafeKeyword())
            review9.addCafeKeyword(createCafeKeyword())
            val reviews = listOf(review1, review2, review3, review4, review5, review6, review7, review8, review9)

            every { feedService.getReviewFeeds(MOCK_MVC_USER_ID, 7L, limit = 3L) } returns FeedWithPagination(
                feeds = reviews.map {
                    FeedInfo(
                        member = FeedMember(createMember()),
                        cafe = FeedCafe(createCafe()),
                        review = FeedReview(
                            id = it.id,
                            finalScore = it.finalScore.score,
                            images = it.images.map(::FeedImage),
                            keywords = it.cafeKeywords.map(::FeedKeyword),
                            likeCount = it.likeCount,
                            description = "친절한 카페",
                            commentCount = 0,
                            isLiked = false,
                            createdAt = it.createdAt
                        )
                    )
                },
                isLastPage = true
            )

            val response = mockMvc.perform(
                get("/feeds/review")
                    .param("minReviewId", "7")
                    .param("limit", "3")
            )

            response.andExpect(
                status().isOk
            ).andDo(
                document(
                    "get-feed-reviews",
                    pathParameters(
                        "minReviewId" means "조회한 피드 리뷰 목록 중 가장 작은 id 값" isOptional true example "5L",
                        "limit" means "한번에 조회하려는 피드 리뷰 개수" isOptional true default "10L" example "10L"
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "isLastPage" type BOOLEAN means "더 조회할 피드가 없는지 여부. 마지막 페이지면 true" example "true",
                        "feeds[].member" type OBJECT means "리뷰를 쓴 유저",
                        "feeds[].member.id" type NUMBER means "유저 id" example "10L",
                        "feeds[].member.name" type STRING means "유저의 닉네임" example "한은주",
                        "feeds[].member.profileImage" type STRING means "유저의 프로필 이미지 URL" example "https://picsum.photos/200",
                        "feeds[].cafe" type OBJECT means "리뷰의 대상인 카페",
                        "feeds[].cafe.id" type NUMBER means "카페 id" example "23L",
                        "feeds[].cafe.name" type STRING means "카페 이름" example "북앤레스트",
                        "feeds[].cafe.address" type STRING means "카페 주소" example "서울시 어딘가",
                        "feeds[].review" type OBJECT means "리뷰 정보",
                        "feeds[].review.id" type NUMBER means "리뷰 id" example "5L",
                        "feeds[].review.finalScore" type NUMBER means "리뷰의 점수" example "4.5",
                        "feeds[].review.images" type ARRAY means "리뷰에 올라간 사진들",
                        "feeds[].review.images[].id" type NUMBER means "리뷰 사진 id" example "15L",
                        "feeds[].review.images[].url" type STRING means "리뷰 사진 url" example "naver.com/some.png",
                        "feeds[].review.likeCount" type NUMBER means "리뷰 좋아요 개수" example "5L",
                        "feeds[].review.keywords" type ARRAY means "리뷰에 있는 키워드 목록",
                        "feeds[].review.keywords[].id" type NUMBER means "키워드 id" example "5L",
                        "feeds[].review.keywords[].keyword" type STRING means "키워드 이름" example "친절한",
                        "feeds[].review.keywords[].emoji" type STRING means "키워드 이모지" example "✨",
                        "feeds[].review.description" type STRING means "리뷰 내용" example "조용하고 좋아요",
                        "feeds[].review.isLiked" type BOOLEAN means "좋아요 여부" example false,
                        "feeds[].review.commentCount" type NUMBER means "리뷰 댓글 개수" example "3L",
                        "feeds[].review.createdAt" type DATETIME means "리뷰가 작성된 시간" formattedAs "yyyy-MM-dd HH:mm:ss" example "2022-02-12 13:52:12"
                    )
                )
            )
        }
    }
}
