package zip.cafe.api

import io.mockk.every
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.post
import zip.cafe.api.dto.CafeSearchRequest
import zip.cafe.api.dto.KeywordSearchRequest
import zip.cafe.api.dto.MemberSearchRequest
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.restdocs.*
import zip.cafe.entity.Food
import zip.cafe.entity.review.Purpose
import zip.cafe.seeds.createCafe
import zip.cafe.seeds.createCafeKeyword
import zip.cafe.seeds.createMember
import zip.cafe.seeds.createReviewCafeKeyword
import zip.cafe.util.Point
import zip.cafe.util.Rectangle

class SearchControllerTest : WebMvcTestAdapter() {
    init {
        "멤버 이름 검색" {
            val memberName = "홍"
            val request = MemberSearchRequest(memberName)

            every { searchService.searchMember(memberName) } returns listOf(createMember(), createMember())

            val response = mockMvc.post("/search/member") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }

            response.andExpect {
                status { isOk() }
            }.andDo {
                documentWithHandle(
                    "search-member-name",
                    requestFields(
                        "name" type STRING means "멤버 이름" example memberName
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        ".id" type NUMBER means "유저 id" example "3",
                        ".name" type STRING means "유저 닉네임" example "홍길동",
                        ".image" type STRING means "유저 이미지 URL" example "https://awsome.image.png",
                        ".description" type STRING means "유저 설명" example "어쩌구 팬카페"
                    )
                )
            }
        }

        "카페 이름 검색" {
            val cafeName = "북카페"
            val visitPurposeList = listOf(Purpose.STUDY, Purpose.DATE)
            val foodList = listOf(Food.BEVERAGE, Food.BAKERY)
            val keywordIdList = listOf(1L, 5L, 3L, 6L)
            val boundary = Rectangle(
                leftTop = Point(latitude = 37.0, longitude = 127.0),
                rightBottom = Point(latitude = 36.0, longitude = 126.0)
            )
            val minCafeId = 1L
            val limit: Long = 30
            val request = CafeSearchRequest(
                name = cafeName,
                visitPurposeList = visitPurposeList,
                foodList = foodList,
                keywordIdList = keywordIdList,
                leftTopLatitude = boundary.leftTop.latitude,
                leftTopLongitude = boundary.leftTop.longitude,
                rightBottomLatitude = boundary.rightBottom.latitude,
                rightBottomLongitude = boundary.rightBottom.longitude,
                minCafeId = minCafeId,
                limit = limit
            )

            every { searchService.searchCafe(cafeName, visitPurposeList, foodList, keywordIdList, boundary, minCafeId, limit) } returns listOf(
                createCafe(name = "멋진 북카페", totalScore = 13.0, reviewCount = 3, footPrintCount = 5),
                createCafe(name = "별로인 북카페", totalScore = 17.0, reviewCount = 5, footPrintCount = 8)
            )

            val response = mockMvc.post("/search/cafe") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }

            response.andExpect {
                status { isOk() }
            }.andDo {
                documentWithHandle(
                    "search-cafe-name",
                    requestFields(
                        "name" type STRING means "카페 이름" example cafeName,
                        "visitPurposeList" type ENUM_ARRAY(Purpose::class) means "방문 목적 리스트",
                        "foodList" type ENUM_ARRAY(Food::class) means "음식 종류 리스트",
                        "keywordIdList" type ARRAY means "키워드 id 리스트" example keywordIdList,
                        "leftTopLatitude" type NUMBER means "좌측 상단 위도" example boundary.leftTop.latitude,
                        "leftTopLongitude" type NUMBER means "좌측 상단 경도" example boundary.leftTop.longitude,
                        "rightBottomLatitude" type NUMBER means "우측 하단 위도" example boundary.rightBottom.latitude,
                        "rightBottomLongitude" type NUMBER means "우측 하단 경도" example boundary.rightBottom.longitude,
                        "minCafeId" type NUMBER means "최소 카페 id" and optional example minCafeId,
                        "limit" type NUMBER means "최대 카페 개수" example limit
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        ".id" type NUMBER means "카페 id" example "5",
                        ".name" type STRING means "카페 네임" example "멋진 북카페",
                        ".image" type STRING means "카페 이미지 URL" example "https://awsome.image.png",
                        ".address" type STRING means "카페 주소" example "서울 강남구 봉은사로 123 5번지 3층",
                        ".numberOfReviews" type NUMBER means "카페 리뷰 개수" example "3",
                        ".numberOfFootPrints" type NUMBER means "카페 발자국 수 개수" example "5",
                        ".averageScore" type NUMBER means "카페 평균 점수" example "4.5",
                        ".position" type OBJECT means "카페 위치",
                        ".position.latitude" type NUMBER means "카페 위도" example "37.123456",
                        ".position.longitude" type NUMBER means "카페 경도" example "127.123456"
                    )
                )
            }
        }

        "키워드 검색" {
            val keyword = "은"
            val request = KeywordSearchRequest(keyword)
            val cafeKeyword1 = createCafeKeyword(id = 1L, keyword = "은은한", emoji = "👐")
            val cafeKeyword2 = createCafeKeyword(id = 2L, keyword = "조용한", emoji = "✌️")

            every { searchService.searchKeyword(keyword) } returns listOf(
                createReviewCafeKeyword(cafeKeyword = cafeKeyword1),
                createReviewCafeKeyword(cafeKeyword = cafeKeyword1),
                createReviewCafeKeyword(cafeKeyword = cafeKeyword2)
            )

            val response = mockMvc.post("/search/keyword") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }

            response.andExpect {
                status { isOk() }
            }.andDo {
                documentWithHandle(
                    "search-keyword",
                    requestFields(
                        "name" type STRING means "키워드" example keyword
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        ".id" type NUMBER means "키워드 id" example "5",
                        ".keyword" type STRING means "키워드" example "은은한",
                        ".numberOfReviews" type NUMBER means "키워드가 포함된 리뷰의 수" example "10"
                    )
                )
            }
        }
    }
}
