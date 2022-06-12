package zip.cafe.api.dto

import io.swagger.v3.oas.annotations.media.Schema
import zip.cafe.entity.Food
import zip.cafe.entity.ReviewImage
import zip.cafe.entity.review.Purpose
import zip.cafe.entity.toScore
import zip.cafe.service.dto.ReviewRegisterDto
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ReviewRegisterRequest(
    @Schema(title = "cafeId", example = "1")
    @field:NotNull(message = "카페 ID가 없습니다")
    val cafeId: Long,

    @Schema(title = "visitPurpose", description = "방문 목적", example = "study")
    @field:NotNull(message = "방문 목적이 없습니다")
    val visitPurpose: Purpose,

    @Schema(title = "visitPurposeScore", description = "방문 만족도 점수", example = "3")
    @field:NotNull(message = "방문 목적 점수가 없습니다")
    val visitPurposeScore: Int,

    @Schema(title = "foodInfos", description = "먹은 음식과 그에 대한 만족도")
    val foodInfos: List<FoodInfo>,

    @Schema(title = "keywords", description = "카페와 관련된 키워드 ID 리스트", example = "[1, 2]")
    val keywords: List<Long>,

    @Schema(title = "reviewImageIds", description = "리뷰 이미지 업로드 API를 통해 받아 왔던 이미지 Id 리스트", example = "[1, 2]")
    val reviewImageIds: List<Long>,

    @Schema(title = "description", description = "리뷰", example = "분위기가 좋아요")
    val description: String,

    @Schema(title = "finalScore", description = "리뷰 최종 점수", example = "3.5")
    @field:NotNull(message = "카페 ID가 존재하지 않습니다")
    val finalScore: Double
) {
    data class FoodInfo(
        @Schema(title = "food", description = "먹은 음식", example = "coffee")
        @field:NotBlank(message = "선택한 음식이 없습니다")
        val food: Food,

        @Schema(title = "score", description = "먹은 음식에 대한 점수", example = "4")
        @field:NotNull(message = "선택한 음식에 대한 점수가 없습니다")
        val score: Int
    )

    fun toDto(): ReviewRegisterDto {
        return ReviewRegisterDto(
            visitPurpose = visitPurpose,
            visitPurposeScore = visitPurposeScore.toScore(),
            foodInfos = foodInfos.map { info -> ReviewRegisterDto.FoodInfo(info.food, info.score.toScore()) },
            reviewImageIds = reviewImageIds,
            keywords = keywords,
            description = description,
            finalScore = finalScore.toScore()
        )
    }
}

data class UploadedImageResponse(
    val id: Long,
    val url: String
) {
    companion object {
        fun from(image: ReviewImage): UploadedImageResponse {
            return UploadedImageResponse(
                id = image.id,
                url = image.cloudFrontURL
            )
        }
    }
}
