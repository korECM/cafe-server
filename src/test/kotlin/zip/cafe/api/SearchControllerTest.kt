package zip.cafe.api

import io.mockk.every
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import zip.cafe.api.utils.restdocs.*
import zip.cafe.seeds.createCafe
import zip.cafe.seeds.createCafeKeyword
import zip.cafe.seeds.createMember
import zip.cafe.seeds.createReviewCafeKeyword

class SearchControllerTest : WebMvcTestAdapter() {
    init {
        "멤버 이름 검색" {
            val memberName = "홍"

            every { searchService.searchMember(memberName) } returns listOf(createMember(), createMember())

            val response = mockMvc.perform(get("/search/member").param("name", memberName))

            response.andExpect(
                status().isOk
            ).andDo(
                document(
                    "search-member-name",
                    requestParameters(
                        "name" means "멤버 이름" example memberName
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        ".id" type NUMBER means "유저 id" example "3",
                        ".name" type STRING means "유저 닉네임" example "홍길동",
                        ".image" type STRING means "유저 이미지 URL" example "https://awsome.image.png",
                        ".description" type STRING means "유저 설명" example "어쩌구 팬카페"
                    )
                )
            )
        }

        "카페 이름 검색" {
            val cafeName = "북카페"

            every { searchService.searchCafe(cafeName) } returns listOf(createCafe(name = "멋진 북카페"), createCafe(name = "별로인 북카페"))

            val response = mockMvc.perform(get("/search/cafe").param("name", cafeName))

            response.andExpect(
                status().isOk
            ).andDo(
                document(
                    "search-cafe-name",
                    requestParameters(
                        "name" means "카페 이름" example cafeName
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        ".id" type NUMBER means "카페 id" example "5",
                        ".name" type STRING means "카페 네임" example "멋진 북카페",
                        ".image" type STRING means "카페 이미지 URL" example "https://awsome.image.png",
                        ".address" type STRING means "카페 주소" example "서울 강남구 봉은사로 123 5번지 3층"
                    )
                )
            )
        }

        "키워드 검색" {
            val keyword = "은"
            val cafeKeyword1 = createCafeKeyword(id = 1L, keyword = "은은한", emoji = "👐")
            val cafeKeyword2 = createCafeKeyword(id = 2L, keyword = "조용한", emoji = "✌️")

            every { searchService.searchKeyword(keyword) } returns listOf(
                createReviewCafeKeyword(cafeKeyword = cafeKeyword1),
                createReviewCafeKeyword(cafeKeyword = cafeKeyword1),
                createReviewCafeKeyword(cafeKeyword = cafeKeyword2)
            )

            val response = mockMvc.perform(get("/search/keyword").param("keyword", keyword))

            response.andExpect(
                status().isOk
            ).andDo(
                document(
                    "search-keyword",
                    requestParameters(
                        "keyword" means "키워드 이름" example keyword
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        ".id" type NUMBER means "키워드 id" example "5",
                        ".keyword" type STRING means "키워드" example "은은한",
                        ".numberOfReviews" type NUMBER means "키워드가 포함된 리뷰의 수" example "10"
                    )
                )
            )
        }
    }
}
