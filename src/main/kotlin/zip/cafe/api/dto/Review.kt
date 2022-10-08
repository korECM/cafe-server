package zip.cafe.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import zip.cafe.config.defaultDateFormat
import zip.cafe.entity.Food
import zip.cafe.entity.ReviewImage
import zip.cafe.entity.review.Purpose
import zip.cafe.entity.toScore
import zip.cafe.service.dto.ReviewRegisterDto
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ReviewRegisterRequest(
    @field:NotNull(message = "카페 ID가 없습니다")
    val cafeId: Long,

    @field:NotNull(message = "방문 목적이 없습니다")
    val visitPurpose: Purpose,

    @field:NotNull(message = "방문 목적 점수가 없습니다")
    val visitPurposeScore: Int,

    val foodInfos: List<FoodInfo>,

    val keywords: List<Long>,

    val reviewImageIds: List<Long>,

    val description: String,

    @field:NotNull(message = "평가 점수가 없습니다")
    val finalScore: Double,

    @JsonFormat(pattern = defaultDateFormat)
    val visitDate: LocalDate
) {
    data class FoodInfo(
        @field:NotBlank(message = "선택한 음식이 없습니다")
        val food: Food,

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
            finalScore = finalScore.toScore(),
        )
    }
}

data class ReviewRegisterFromFootprintRequest(
    @field:NotNull(message = "방문 목적이 없습니다")
    val visitPurpose: Purpose,

    @field:NotNull(message = "방문 목적 점수가 없습니다")
    val visitPurposeScore: Int,

    val foodInfos: List<FoodInfo>,

    val keywords: List<Long>,

    val reviewImageIds: List<Long>,

    val description: String,

    @field:NotNull(message = "평가 점수가 없습니다")
    val finalScore: Double,
) {
    data class FoodInfo(
        @field:NotBlank(message = "선택한 음식이 없습니다")
        val food: Food,

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
            finalScore = finalScore.toScore(),
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
