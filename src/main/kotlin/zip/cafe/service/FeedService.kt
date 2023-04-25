package zip.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.api.dto.*
import zip.cafe.repository.MemberFollowRepository
import zip.cafe.repository.ReviewRepository
import kotlin.math.max

private const val MIN_FOLLOWEE_SIZE = 3

@Transactional(readOnly = true)
@Service
class FeedService(
    private val reviewRepository: ReviewRepository,
    private val memberFollowRepository: MemberFollowRepository
) {

    fun getReviewFeeds(loginMemberId: Long, minReviewIdInFeed: Long?, limit: Long): FeedWithPagination {
        val followeeIds :MutableList<Long> = memberFollowRepository.getFolloweeIds(loginMemberId).toMutableList()
        if (followeeIds.size < MIN_FOLLOWEE_SIZE) {
            val popularMemberIds = memberFollowRepository.getPopularMemberIds(max(MIN_FOLLOWEE_SIZE, followeeIds.size))
            for (popularMemberId in popularMemberIds) {
                if (popularMemberId !in followeeIds) {
                    followeeIds += popularMemberId
                }
            }
        }
        val footprints = reviewRepository.findByAuthorIdIn(followeeIds + loginMemberId, minReviewIdInFeed, limit)
        val isLastPage = minReviewIdInFeed?.let { reviewRepository.isLastPageByAuthorIds(followeeIds, minReviewIdInFeed, limit) } ?: false
        val reviewAndLikes = reviewRepository.findReviewsAndLikesOnThoseReviews(loginMemberId, footprints.mapNotNull { it.review?.id })
        return FeedWithPagination(
            feeds = footprints.map { footprint ->
                FeedInfo(
                    member = FeedMember(footprint.member),
                    cafe = FeedCafe(footprint.cafe),
                    review = FeedReview(
                        id = footprint.review!!.id,
                        finalScore = footprint.review!!.finalScore.score,
                        images = footprint.review!!.images.map(::FeedImage),
                        keywords = footprint.review!!.cafeKeywords.map(::FeedKeyword),
                        likeCount = footprint.review!!.likeCount,
                        description = footprint.review!!.description,
                        isLiked = reviewAndLikes.getOrDefault(footprint.review!!.id, false),
                        commentCount = footprint.review!!.commentCount,
                        createdAt = footprint.createdAt
                    )
                )
            }.sortedByDescending { it.review.id },
            isLastPage = isLastPage
        )
    }
}
