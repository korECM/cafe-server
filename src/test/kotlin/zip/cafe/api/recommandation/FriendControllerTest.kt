package zip.cafe.api.recommandation

import io.mockk.every
import org.springframework.test.web.servlet.get
import zip.cafe.api.WebMvcTestAdapter
import zip.cafe.api.recommandation.dto.FriendRecommendationResult
import zip.cafe.api.recommandation.dto.FriendRecommendationReviewImage
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.restdocs.*
import zip.cafe.seeds.MOCK_MVC_USER_ID

class FriendControllerTest : WebMvcTestAdapter() {
    init {
        "추천 멤버 조회" {
            val loginMemberId = MOCK_MVC_USER_ID

            every { recommendationService.getFriendRecommendation(loginMemberId) } returns listOf(
                FriendRecommendationResult(
                    memberId = 1,
                    profileImageURL = "https://cafezip.s3.ap-northeast-2.amazonaws.com/profiles/1.png",
                    numberOfReviews = 5,
                    numberOfFollower = 3,
                    reviewImages = listOf(
                        FriendRecommendationReviewImage(
                            id = 5,
                            imageURL = "https://cafezip.s3.ap-northeast-2.amazonaws.com/reviews/5.png"
                        ),
                        FriendRecommendationReviewImage(
                            id = 6,
                            imageURL = "https://cafezip.s3.ap-northeast-2.amazonaws.com/reviews/6.png"
                        )
                    )
                ),
                FriendRecommendationResult(
                    memberId = 2,
                    profileImageURL = "https://cafezip.s3.ap-northeast-2.amazonaws.com/profiles/2.png",
                    numberOfReviews = 10,
                    numberOfFollower = 5,
                    reviewImages = listOf(
                        FriendRecommendationReviewImage(
                            id = 7,
                            imageURL = "https://cafezip.s3.ap-northeast-2.amazonaws.com/reviews/7.png"
                        ),
                        FriendRecommendationReviewImage(
                            id = 8,
                            imageURL = "https://cafezip.s3.ap-northeast-2.amazonaws.com/reviews/8.png"
                        )
                    )
                )
            )

            val response = mockMvc.get("/recommendation/friends")

            response.andExpect {
                status { isOk() }
            }.andDo {
                documentWithHandle(
                    "get-recommendation-friends",
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "friends" type ARRAY means "추천 친구 목록",
                        "friends[].memberId" type NUMBER means "추천 친구의 Id" example "1L",
                        "friends[].profileImageURL" type STRING means "추천 친구의 프로필 이미지 URL" example "https://cafezip.s3.ap-northeast-2.amazonaws.com/profiles/1.png",
                        "friends[].numberOfReviews" type NUMBER means "추천 친구의 리뷰 개수" example "5",
                        "friends[].numberOfFollower" type NUMBER means "추천 친구의 팔로워 수" example "3",
                        "friends[].reviewImages" type ARRAY means "추천 친구의 리뷰 이미지 목록",
                        "friends[].reviewImages[].id" type NUMBER means "추천 친구의 리뷰 이미지 Id" example "5L",
                        "friends[].reviewImages[].imageURL" type STRING means "추천 친구의 리뷰 이미지 URL" example "https://cafezip.s3.ap-northeast-2.amazonaws.com/reviews/5.png"
                    )
                )
            }
        }

    }
}