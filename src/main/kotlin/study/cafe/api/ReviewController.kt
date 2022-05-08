package study.cafe.api

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import study.cafe.api.dto.ApiResponse
import study.cafe.api.dto.ApiResponse.Companion.success
import study.cafe.api.dto.ReviewRegisterRequest
import study.cafe.api.dto.UploadedImageResponse
import study.cafe.api.dto.UploadedImageResponse.Companion.from
import study.cafe.entity.member.Member
import study.cafe.security.LoginUser
import study.cafe.security.LoginUserId
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
        @LoginUser member: Member,
        @Valid @RequestBody request: ReviewRegisterRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        println("dto = $request")

        val cafe = cafeService.findById(request.cafeId)
        reviewService.createReview(request.toDto(cafe, member))
        return status(CREATED).body(success(null))
    }

    @PostMapping("/images")
    fun uploadImages(
        @LoginUserId userId: Long,
        @RequestPart("images") images: List<MultipartFile>,
    ): List<UploadedImageResponse> {
        val uploadReviewImages = reviewService.uploadReviewImages(userId, images)
        return uploadReviewImages.map { from(it) }
    }
}
