package zip.cafe.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import zip.cafe.entity.ReviewImage
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.member.Member
import zip.cafe.entity.review.CafeKeyword
import java.time.LocalDateTime

data class FeedInfo(
    val id: Long,
    val member: FeedMember,
    val cafe: FeedCafe,
    val review: FeedReview
)

data class FeedMember(
    val id: Long,
    val name: String,
    val profileImage: String,
) {
    constructor(member: Member) : this(member.id, member.nickname, member.profileImage)
}

data class FeedCafe(
    val id: Long,
    val name: String,
    val address: String,
) {
    constructor(cafe: Cafe) : this(cafe.id, cafe.name, cafe.address)
}

data class FeedReview(
    val finalScore: Double,
    val images: List<FeedImage>,
    val keyword: List<FeedKeyword>,
    val likeCount: Int,
    val content: String,
    val commentCount: Int,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime
)

data class FeedImage(
    val id: Long,
    val url: String
) {
    constructor(reviewImage: ReviewImage) : this(reviewImage.id, reviewImage.cloudFrontURL)
}

data class FeedKeyword(
    val id: Long,
    val name: String,
    val emoji: String
) {
    constructor(cafeKeyword: CafeKeyword) : this(cafeKeyword.id, cafeKeyword.keyword, cafeKeyword.emoji)
}
