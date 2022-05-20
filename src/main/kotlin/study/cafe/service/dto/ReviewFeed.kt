package study.cafe.service.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class ReviewFeed @QueryProjection constructor(
    val cafeId: Long,
    val cafeName: String,
    val cafeAddress: String,
    val memberId: Long,
    val memberName: String,
    val reviewFinalScore: Double,
    val reviewLikeCount: Long,
    val reviewCommentCount: Long,
    val reviewCreatedAt: LocalDateTime,
    val reviewImageId: Long,
    val reviewImageUrl: String,
    val reviewKeywordId: Long,
    val reviewKeywordName: String,
    val reviewKeywordEmoji: String,
)
