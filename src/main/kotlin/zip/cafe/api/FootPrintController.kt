package zip.cafe.api

import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.FootPrintRegisterRequest
import zip.cafe.api.dto.FootPrintRegisterResponse
import zip.cafe.api.dto.ReviewRegisterFromFootprintRequest
import zip.cafe.security.LoginUserId
import zip.cafe.service.ReviewService
import javax.validation.Valid

@RequestMapping("/footprints")
@RestController
class FootPrintController(
    private val reviewService: ReviewService,
) {
    @ResponseStatus(CREATED)
    @PostMapping
    fun register(
        @LoginUserId userId: Long,
        @Valid @RequestBody request: FootPrintRegisterRequest
    ): ApiResponse<FootPrintRegisterResponse> {
        val createdFootprintId = reviewService.createFootprint(request.cafeId, userId, request.visitDate)
        return success(FootPrintRegisterResponse(createdFootprintId))
    }

    @ResponseStatus(CREATED)
    @PostMapping("/{footprintId}/reviews")
    fun registerFromFootprint(
        @PathVariable footprintId: Long,
        @LoginUserId userId: Long,
        @Valid @RequestBody request: ReviewRegisterFromFootprintRequest,
    ): ApiResponse<Nothing> {
        reviewService.createReview(footprintId, userId, request.toDto())
        return success(null)
    }
}
