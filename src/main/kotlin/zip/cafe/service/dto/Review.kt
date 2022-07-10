package zip.cafe.service.dto

import zip.cafe.entity.FloatScore
import zip.cafe.entity.Food
import zip.cafe.entity.IntScore
import zip.cafe.entity.review.Purpose

data class ReviewRegisterDto(
    val visitPurpose: Purpose,
    val visitPurposeScore: IntScore,
    val foodInfos: List<FoodInfo>,
    val keywords: List<Long>,
    val reviewImageIds: List<Long>,
    val description: String,
    val finalScore: FloatScore
) {

    data class FoodInfo(
        val food: Food,
        val score: IntScore
    )
}

data class ReviewSummary(
    val numberOfReviews: Long,
    val averageOfFinalScores: Double
)

data class FollowerWhoWriteReview(
    val id: Long,
    val name: String
)
