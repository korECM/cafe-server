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
import study.cafe.security.LoginUserId
import study.cafe.service.ReviewService
import javax.validation.Valid

@RequestMapping("/review")
@RestController
class ReviewController(
    private val reviewService: ReviewService
) {

    @PostMapping("")
    fun register(
        @LoginUserId userId: Long,
        @Valid @RequestBody request: ReviewRegisterRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        reviewService.createReview(request.cafeId, userId, request.toDto())
        return status(CREATED).body(success(null))
    }

    @PostMapping("/images")
    fun uploadImages(
        @LoginUserId userId: Long,
        @RequestPart("images") images: List<MultipartFile>,
    ): ResponseEntity<ApiResponse<List<UploadedImageResponse>>> {
        val uploadReviewImages = reviewService.uploadReviewImages(userId, images)
        return status(CREATED).body(success(uploadReviewImages.map { from(it) }))
    }
}
