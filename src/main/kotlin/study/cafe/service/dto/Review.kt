package study.cafe.service.dto

import study.cafe.entity.*

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

data class UploadedReviewImage(
    val id: Long,
    val bucket: String,
    val key: String,
    val s3URL: String,
    val cloudFrontURL: String
) {
    companion object {
        fun from(reviewImage: ReviewImage): UploadedReviewImage {
            return UploadedReviewImage(
                id = reviewImage.id,
                bucket = reviewImage.bucket,
                key = reviewImage.fileKey,
                s3URL = reviewImage.s3URL,
                cloudFrontURL = reviewImage.cloudFrontURL
            )
        }
    }
}
