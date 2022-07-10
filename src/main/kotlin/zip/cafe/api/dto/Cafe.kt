package zip.cafe.api.dto

import zip.cafe.entity.ReviewImage
import zip.cafe.entity.review.CafeKeyword

data class SingleCafeInfo(
    val id: Long,
    val name: String,
    val address: String,
    val openingHours: String,
    val menu: List<Menu>,
    val averageOfFinalScores: Double,
    val reviewCount: Long,
    val keywords: List<Keyword>,
    val cafeImages: List<Image>
) {

    data class Keyword(
        val id: Long,
        val keyword: String,
        val emoji: String,
    ) {
        companion object {
            fun from(cafeKeyword: CafeKeyword) = Keyword(cafeKeyword.id, cafeKeyword.keyword, cafeKeyword.emoji)
        }
    }

    data class Image(
        val id: Long,
        val url: String
    ) {
        companion object {
            fun from(image: ReviewImage) = Image(image.id, image.cloudFrontURL)
        }
    }

    data class Menu(
        val id: Long,
        val name: String,
        val price: Long
    )
}
