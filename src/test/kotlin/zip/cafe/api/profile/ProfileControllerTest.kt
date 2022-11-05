package zip.cafe.api.profile

import io.mockk.every
import zip.cafe.api.WebMvcTestAdapter
import zip.cafe.api.profile.dto.*
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.mockmvc.getWithPathParameter
import zip.cafe.api.utils.restdocs.*
import zip.cafe.config.formatAsDefault
import zip.cafe.entity.member.Member
import zip.cafe.seeds.MOCK_MVC_USER_ID
import java.time.LocalDate
import java.time.LocalDateTime.now

class ProfileControllerTest : WebMvcTestAdapter() {
    init {
        "유저의 프로필 조회" {
            val loginMemberId = MOCK_MVC_USER_ID
            val targetMemberId = 5L

            every { profileService.getProfile(loginMemberId, targetMemberId) } returns ProfileInfo(
                id = targetMemberId,
                nickname = "홍길동",
                profileImageURL = Member.DEFAULT_PROFILE_IMAGE_URL,
                description = "적당한 설명",
                numberOfReview = 10,
                numberOfFootprint = 20,
                numberOfFollowers = 151612,
                numberOfFollowees = 165,
                following = true
            )

            val response = mockMvc.getWithPathParameter("/profiles/members/{targetMemberId}", targetMemberId)

            response.andExpect {
                status { isOk() }
            }.andDo {
                documentWithHandle(
                    "get-profile",
                    pathParameters(
                        "targetMemberId" means "프로필을 조회하려는 유저의 Id" example "5L"
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "id" type NUMBER means "유저의 Id" example "5L",
                        "nickname" type STRING means "유저의 닉네임" example "홍길동",
                        "profileImageURL" type STRING means "유저의 프로필 이미지 URL" example "https://cafezip.s3.ap-northeast-2.amazonaws.com/profiles/5L.png",
                        "description" type STRING means "유저의 설명" example "적당한 설명",
                        "numberOfReview" type NUMBER means "유저의 리뷰 수" example 10,
                        "numberOfFootprint" type NUMBER means "유저의 발자국 수" example 20,
                        "numberOfFollowers" type NUMBER means "유저의 팔로워 수" example "151612",
                        "numberOfFollowees" type NUMBER means "유저의 팔로잉 수" example "165",
                        "following" type BOOLEAN means "로그인한 유저가 해당 유저를 팔로우하고 있는지 여부" example true
                    )
                )
            }
        }

        "유저의 프로필에 있는 리뷰 조회" {
            val memberId = 5L

            every { profileService.getReview(memberId) } returns listOf(
                ProfileReviewInfo(
                    id = 1,
                    cafe = ProfileCafeInfo(id = 3, name = "북앤레스트", address = "서울시 강남구"),
                    images = listOf(
                        ProfileReviewImageInfo(id = 1L, url = "https://media-cdn.tripadvisor.com/media/photo-s/10/e5/73/92/photo1jpg.jpg"),
                        ProfileReviewImageInfo(id = 2L, url = "https://vinesoftheyarravalley.com.au/wp-content/uploads/2021/02/cafe-vines-1024x702.jpg")
                    ),
                    finalScore = 4.0,
                    likeCount = 3,
                    content = "조용하고 좋아요",
                    commentCount = 5,
                    createdAt = now().minusDays(3)
                ),
                ProfileReviewInfo(
                    id = 2,
                    cafe = ProfileCafeInfo(id = 4, name = "", address = "서울시 영등포구"),
                    images = listOf(
                        ProfileReviewImageInfo(id = 3L, url = "https://media-cdn.tripadvisor.com/media/photo-s/10/e5/73/92/photo1jpg.jpg"),
                        ProfileReviewImageInfo(id = 4L, url = "https://vinesoftheyarravalley.com.au/wp-content/uploads/2021/02/cafe-vines-1024x702.jpg")
                    ),
                    finalScore = 3.0,
                    likeCount = 2,
                    content = "조금 시끄러워요",
                    commentCount = 123,
                    createdAt = now().minusDays(1)
                )
            )

            val response = mockMvc.getWithPathParameter("/profiles/members/{memberId}/reviews", memberId)

            response.andExpect {
                status { isOk() }
            }.andDo {
                documentWithHandle(
                    "get-profile-reviews",
                    pathParameters(
                        "memberId" means "프로필을 조회하려는 유저의 Id" example "5L"
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "id" type NUMBER means "유저의 Id" example "5L",
                        "cafe" type OBJECT means "카페 정보",
                        "cafe.id" type NUMBER means "카페의 Id" example "3",
                        "cafe.name" type STRING means "카페의 이름" example "북앤레스트",
                        "cafe.address" type STRING means "카페의 주소" example "서울시 강남구",
                        "images" type ARRAY means "리뷰에 포함된 이미지 정보",
                        "images[].id" type NUMBER means "이미지의 Id" example "1",
                        "images[].url" type STRING means "이미지의 URL" example "https://media-cdn.tripadvisor.com/media/photo-s/10/e5/73/92/photo1jpg.jpg",
                        "finalScore" type NUMBER means "리뷰의 최종 점수" example "4.0",
                        "likeCount" type NUMBER means "리뷰의 좋아요 수" example "3",
                        "content" type STRING means "리뷰의 내용" example "조용하고 좋아요",
                        "commentCount" type NUMBER means "리뷰의 댓글 수" example "5",
                        "createdAt" type DATE means "리뷰의 작성 시간" example now().formatAsDefault()
                    )
                )
            }
        }

        "유저의 프로필에 있는 발자국 조회" {
            val memberId = 5L

            every { profileService.getFootprint(memberId) } returns listOf(
                ProfileFootprintInfo(
                    id = 1,
                    cafe = ProfileCafeInfo(id = 3, name = "북앤레스트", address = "서울시 강남구"),
                    visitDate = LocalDate.now().minusDays(3),
                    reviewId = null
                ),
                ProfileFootprintInfo(
                    id = 2,
                    cafe = ProfileCafeInfo(id = 4, name = "", address = "서울시 영등포구"),
                    visitDate = LocalDate.now().minusDays(3),
                    reviewId = 5
                )
            )

            val response = mockMvc.getWithPathParameter("/profiles/members/{memberId}/footprints", memberId)

            response.andExpect {
                status { isOk() }
            }.andDo {
                documentWithHandle(
                    "get-profile-footprints",
                    pathParameters(
                        "memberId" means "프로필을 조회하려는 유저의 Id" example "5L"
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "id" type NUMBER means "유저의 Id" example "5L",
                        "cafe" type OBJECT means "카페 정보",
                        "cafe.id" type NUMBER means "카페의 Id" example "3",
                        "cafe.name" type STRING means "카페의 이름" example "북앤레스트",
                        "cafe.address" type STRING means "카페의 주소" example "서울시 강남구",
                        "visitDate" type DATE means "방문 날짜" example LocalDate.now().formatAsDefault(),
                        "reviewId" type NUMBER means "리뷰의 Id" example "5" and optional
                    )
                )
            }
        }
    }
}
