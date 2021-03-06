package zip.cafe.api.dto

import zip.cafe.entity.ReviewImage
import zip.cafe.entity.menu.Menu
import zip.cafe.entity.review.CafeKeyword
import zip.cafe.service.dto.FollowerWhoLikeCafe
import zip.cafe.service.dto.FollowerWhoWriteReview

data class SingleCafeInfo(
    val id: Long,
    val name: String,
    val address: String,
    val openingHours: String,
    val menus: List<InnerMenu>,
    val averageOfFinalScores: Double,
    val reviewCount: Long,
    val keywords: List<Keyword>,
    val cafeImages: List<Image>,
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

    data class InnerMenu(
        val id: Long,
        val name: String,
        val price: Long
    ) {
        companion object {
            fun from(menu: Menu) = InnerMenu(id = menu.id, name = menu.name, price = menu.price)
        }
    }
}

data class FollowersWhoWriteReview(
    val followersWhoWriteReview: List<InnerFollowersWhoWriteReview>,
) {
    data class InnerFollowersWhoWriteReview(
        val id: Long,
        val name: String
    ) {
        companion object {
            fun from(data: FollowerWhoWriteReview) = InnerFollowersWhoWriteReview(id = data.id, name = data.name)
        }
    }
}

data class FollowersWhoLikeCafe(
    val followersWhoLikeCafe: List<InnerFollowersWhoLikeCafe>,
) {
    data class InnerFollowersWhoLikeCafe(
        val id: Long,
        val name: String
    ) {
        companion object {
            fun from(data: FollowerWhoLikeCafe) = InnerFollowersWhoLikeCafe(id = data.id, name = data.name)
        }
    }
}
