package study.cafe.api

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import study.cafe.api.dto.ApiResponse
import study.cafe.api.dto.ApiResponse.Companion.success
import study.cafe.api.dto.ReviewRegisterRequest
import study.cafe.entity.member.Member
import study.cafe.security.LoginUser
import study.cafe.service.CafeService
import study.cafe.service.ReviewService
import javax.validation.Valid

@RequestMapping("/review")
@RestController
class ReviewController(
    private val reviewService: ReviewService,
    private val cafeService: CafeService
) {

    @PostMapping("")
    fun register(
        @Parameter(hidden = true) @LoginUser member: Member,
        @Valid @RequestBody request: ReviewRegisterRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        println("dto = $request")

        val cafe = cafeService.findById(request.cafeId)
        reviewService.createReview(request.toDto(cafe, member))
        return status(CREATED).body(success(null))
    }
}
