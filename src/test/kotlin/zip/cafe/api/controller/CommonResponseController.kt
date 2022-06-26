package zip.cafe.api.controller

import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success

@RestController
class CommonResponseController {

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @GetMapping("/common/error")
    fun commonError() = ApiResponse.error("해당 유저가 존재하지 않습니다")

    @GetMapping("/common/single")
    fun commonSingle() = success(TestClass(1L, "리뷰 내용"))

    @GetMapping("/common/list")
    fun commonList() = success(listOf(TestClass(1L, "리뷰 내용1"), TestClass(2L, "리뷰 내용 2")))

    @GetMapping("/common/null")
    fun commonNull() = success(null)

    class TestClass(
        val id: Long,
        val content: String
    )
}
