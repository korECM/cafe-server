package zip.cafe.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import zip.cafe.config.defaultDateTimeFormat
import zip.cafe.entity.Point
import zip.cafe.entity.ReviewImage
import zip.cafe.entity.cafe.CafeKeywordStat
import zip.cafe.entity.member.Member
import zip.cafe.entity.menu.Menu
import zip.cafe.entity.review.CafeKeyword
import zip.cafe.service.dto.FollowerWhoLikeCafe
import zip.cafe.service.dto.FollowerWhoWriteReview
import java.time.LocalDateTime

data class SingleCafeInfo(
    val id: Long,
    val name: String,
    val address: String,
    val location: Point,
    val openingHours: String,
    val menus: List<InnerMenu>,
    val countOfReviewByFollowee: Long,
    val reviewScoreStat: Map<Double, Long>,
    val keywords: List<Keyword>,
    val cafeImages: List<Image>,
    val reviewImages: List<Image>
) {

    data class Keyword(
        val id: Long,
        val keyword: String,
        val emoji: String,
        val count: Long,
    ) {
        companion object {
            fun from(cafeKeywordStat: CafeKeywordStat) = with(cafeKeywordStat.keyword) { Keyword(id, keyword, emoji, cafeKeywordStat.count) }
        }
    }

    data class Image(
        val id: Long,
        val url: String
    ) {
        companion object {
            fun from(image: ReviewImage) = with(image) { Image(id, cloudFrontURL) }
        }
    }

    data class InnerMenu(
        val id: Long,
        val name: String,
        val price: Long
    ) {
        companion object {
            fun from(menu: Menu) = with(menu) { InnerMenu(id = id, name = name, price = price) }
        }
    }
}

data class ReviewWithPagination(
    val reviews: List<ReviewForCafeInfo>,
    val isLastPage: Boolean,
)

data class ReviewForCafeInfo(
    val id: Long,
    val member: ReviewMemberInfo,
    val review: ReviewInfo,
) {

    data class ReviewMemberInfo(
        val id: Long,
        val name: String,
        val profileImage: String,
    ) {
        constructor(member: Member) : this(member.id, member.nickname, member.profileImage)
    }

    data class ReviewInfo(
        val id: Long,
        val finalScore: Double,
        val images: List<ReviewImageInfo>,
        val keywords: List<ReviewKeywordInfo>,
        val likeCount: Long,
        val description: String,
        val commentCount: Long,
        val isLiked: Boolean,
        val isFolloweeReview: Boolean,
        @JsonFormat(pattern = defaultDateTimeFormat)
        val createdAt: LocalDateTime
    )

    data class ReviewImageInfo(
        val id: Long,
        val url: String
    ) {
        constructor(reviewImage: ReviewImage) : this(reviewImage.id, reviewImage.cloudFrontURL)
    }

    data class ReviewKeywordInfo(
        val id: Long,
        val keyword: String,
        val emoji: String,
    ) {
        constructor(cafeKeyword: CafeKeyword) : this(cafeKeyword.id, cafeKeyword.keyword, cafeKeyword.emoji)
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
