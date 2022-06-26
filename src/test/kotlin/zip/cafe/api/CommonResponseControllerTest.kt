package zip.cafe.api

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.get
import zip.cafe.api.controller.CommonResponseController
import zip.cafe.api.utils.restdocs.*
import zip.cafe.api.utils.spec.WebMvcTestSpec

@WebMvcTest(CommonResponseController::class)
class CommonResponseControllerTest : WebMvcTestSpec() {
    init {
        "공통 응답 null" {
            val response = mockMvc.get("/common/null")

            response.andExpect {
                status { isOk() }
            }
                .andDo {
                    handle(
                        document(
                            "common-response-null",
                            responseBody(
                                "message" type STRING means "응답 메시지" example "",
                                "body" type NULL means "데이터" example "null",
                            )
                        )
                    )
                }
        }
        "공통 응답 single" {
            val response = mockMvc.get("/common/single")

            response.andExpect {
                status { isOk() }
            }
                .andDo {
                    handle(
                        document(
                            "common-response-single",
                            responseBody(
                                "message" type STRING means "응답 메시지" example "",
                                "body" type OBJECT means "데이터" example "{id: 1, content: \"리뷰 내용\"}",
                                "body.id" type NUMBER means "응답 데이터 예시" example "1L",
                                "body.content" type STRING means "응답 데이터 예시" example "리뷰 내용"
                            )
                        )
                    )
                }
        }

        "공통 응답 list" {
            val response = mockMvc.get("/common/list")

            response.andExpect {
                status { isOk() }
            }
                .andDo {
                    handle(
                        document(
                            "common-response-list",
                            responseBody(
                                "message" type STRING means "응답 메시지" example "",
                                "body" type ARRAY means "데이터" example "[{id: 1, content: \"리뷰 내용1\"}, {id: 2, content: \"리뷰 내용2\"}]",
                                "body[].id" type NUMBER means "응답 데이터 예시" example "1L",
                                "body[].content" type STRING means "응답 데이터 예시" example "리뷰 내용1"
                            )
                        )
                    )
                }
        }

        "공통 응답 에러" {
            val response = mockMvc.get("/common/error")

            response.andExpect {
                status { isInternalServerError() }
            }
                .andDo {
                    handle(
                        document(
                            "common-response-error",
                            responseBody(
                                "message" type STRING means "에러 메시지" example "해당 유저가 존재하지 않습니다",
                                "body" type NULL means "데이터" example "null",
                            )
                        )
                    )
                }
        }
    }
}
