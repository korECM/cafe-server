package zip.cafe.api

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.ReviewRegisterRequest
import zip.cafe.api.dto.UploadedImageResponse
import zip.cafe.api.dto.UploadedImageResponse.Companion.from
import zip.cafe.security.LoginUserId
import zip.cafe.service.ReviewLikeService
import zip.cafe.service.ReviewService
import javax.validation.Valid

@RequestMapping("/reviews")
@RestController
class ReviewController(
    private val reviewService: ReviewService,
    private val reviewLikeService: ReviewLikeService
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
        val savedReviewImages = reviewService.run {
            val uploadReviewImages = uploadReviewImages(images)
            saveUploadedReviewImage(userId, uploadReviewImages)
        }
        return status(CREATED).body(success(savedReviewImages.map { from(it) }))
    }

    @ResponseStatus(CREATED)
    @PostMapping("/{reviewId}/like")
    fun likeReview(@LoginUserId userId: Long, @PathVariable("reviewId") reviewId: Long): ApiResponse<Nothing> {
        reviewLikeService.likeReview(userId, reviewId)
        return success(null)
    }

    @ResponseStatus(NO_CONTENT)
    @PostMapping("/{reviewId}/unlike")
    fun unlikeReview(@LoginUserId userId: Long, @PathVariable("reviewId") reviewId: Long): ApiResponse<Nothing> {
        reviewLikeService.cancelLikeReview(userId, reviewId)
        return success(null)
    }
}
