package study.cafe.api.dto

import java.time.LocalDateTime

data class FeedInfo(
    val member: FeedMember,
    val cafe: FeedCafe,
    val review: FeedReview
)

data class FeedMember(
    val id: Long,
    val name: String
)

data class FeedCafe(
    val id: Long,
    val name: String,
    val address: String,
)

data class FeedReview(
    val finalScore: Double,
    val images: List<FeedImage>,
    val keyword: List<FeedKeyword>,
    val likeCount: Long,
    val commentCount: Long,
    val createdAt: LocalDateTime
)

data class FeedImage(
    val id: Long,
    val url: String
)

data class FeedKeyword(
    val id: Long,
    val name: String,
    val emoji: String
)
