package zip.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.api.dto.*
import zip.cafe.repository.MemberFollowRepository
import zip.cafe.repository.ReviewRepository

@Transactional(readOnly = true)
@Service
class FeedService(
    private val reviewRepository: ReviewRepository,
    private val memberFollowRepository: MemberFollowRepository
) {

    fun getReviewFeeds(loginMemberId: Long, minReviewIdInFeed: Long?, limit: Long): List<FeedInfo> {
        val followeeIds = memberFollowRepository.getFolloweeIds(loginMemberId)
        val reviews = reviewRepository.findByAuthorIdIn(followeeIds + loginMemberId, minReviewIdInFeed, limit)
        val isLastPage = minReviewIdInFeed?.let { reviewRepository.isLastPage(followeeIds, minReviewIdInFeed, limit) } ?: false
        return reviews.map { review ->
            FeedInfo(
                id = review.id,
                member = FeedMember(review.member),
                cafe = FeedCafe(review.cafe),
                isLastPage = isLastPage,
                review = FeedReview(
                    finalScore = review.finalScore.score,
                    images = review.images.map(::FeedImage),
                    keyword = review.cafeKeywords.map(::FeedKeyword),
                    likeCount = review.likers.size,
                    content = review.description,
                    commentCount = 0,
                    createdAt = review.createdAt
                )
            )
        }.sortedByDescending { it.id }
    }
}
