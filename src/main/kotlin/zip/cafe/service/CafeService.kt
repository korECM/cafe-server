package zip.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.api.dto.ReviewForCafeInfo
import zip.cafe.api.dto.ReviewForCafeInfo.ReviewImageInfo
import zip.cafe.api.dto.ReviewForCafeInfo.ReviewKeywordInfo
import zip.cafe.api.dto.ReviewWithPagination
import zip.cafe.api.dto.ReviewWithoutPagination
import zip.cafe.entity.cafe.CafeKeywordStat
import zip.cafe.repository.CafeRepository
import zip.cafe.repository.MemberFollowRepository
import zip.cafe.repository.ReviewRepository
import zip.cafe.repository.findOneById
import zip.cafe.service.dto.FollowerWhoLikeCafe
import zip.cafe.service.dto.FollowerWhoWriteReview

@Transactional(readOnly = true)
@Service
class CafeService(
    private val cafeRepository: CafeRepository,
    private val reviewRepository: ReviewRepository,
    private val memberFollowRepository: MemberFollowRepository
) {

    fun findById(id: Long) = cafeRepository.findOneById(id)

    fun findByIdForDetailPage(cafeId: Long) = cafeRepository.findOneByIdForDetail(cafeId)

    fun getReviewSummaryById(cafeId: Long) = reviewRepository.getReviewSummaryByCafeId(cafeId)

    fun getReviewImageSummaryById(cafeId: Long) = reviewRepository.getImageSummaryByCafeId(cafeId)

    fun getKeywordSummaryById(cafeId: Long): List<CafeKeywordStat> = cafeRepository.getKeywordSummaryByCafeId(cafeId)

    fun findFollowerWhoWriteReview(memberId: Long, cafeId: Long): List<FollowerWhoWriteReview> {
        val followeeIds = memberFollowRepository.getFolloweeIds(memberId)
        return reviewRepository.findWhoWriteReview(followeeIds, cafeId)
    }

    fun findFollowerWhoLikeCafe(memberId: Long, cafeId: Long): List<FollowerWhoLikeCafe> {
        val followeeIds = memberFollowRepository.getFolloweeIds(memberId)
        return cafeRepository.findWhoLikeCafe(followeeIds, cafeId)
    }

    fun getFriendReviewCountByCafeId(cafeId: Long, userId: Long): Long {
        val followeeIds = memberFollowRepository.getFolloweeIds(userId)
        return reviewRepository.getReviewCountByCafeIdAndUserId(cafeId, followeeIds)
    }

    fun getDetailReviewsByCafeIdAndUserId(cafeId: Long, loginMemberId: Long?, minReviewIdInCafeDetail: Long?, limit: Long): ReviewWithPagination {
        // TODO 검색 필터링 기능 추가, 정렬 기능 추가
        val followeeIds = loginMemberId?.let { memberFollowRepository.getFolloweeIds(loginMemberId) } ?: emptyList()
        val footprints = reviewRepository.findByCafeId(cafeId, minReviewIdInCafeDetail, limit)
        val isLastPage = minReviewIdInCafeDetail?.let { reviewRepository.isLastPageByCafeId(cafeId, minReviewIdInCafeDetail, limit) } ?: true
        val reviewAndLikes =
            loginMemberId?.let { reviewRepository.findReviewsAndLikesOnThoseReviews(loginMemberId, footprints.mapNotNull { it.review?.id }) } ?: emptyMap()
        return ReviewWithPagination(
            reviews = footprints.map { footprint ->
                ReviewForCafeInfo(
                    id = footprint.review!!.id,
                    member = ReviewForCafeInfo.ReviewMemberInfo(footprint.member),
                    review = ReviewForCafeInfo.ReviewInfo(
                        id = footprint.review!!.id,
                        finalScore = footprint.review!!.finalScore.score,
                        images = footprint.review!!.images.map(::ReviewImageInfo),
                        keywords = footprint.review!!.cafeKeywords.map(::ReviewKeywordInfo),
                        likeCount = footprint.review!!.likeCount,
                        description = footprint.review!!.description,
                        isLiked = reviewAndLikes.getOrDefault(footprint.review!!.id, false),
                        isFolloweeReview = followeeIds.contains(footprint.member.id),
                        commentCount = footprint.review!!.commentCount,
                        createdAt = footprint.createdAt
                    )
                )
            }.sortedByDescending { it.review.id },
            isLastPage = isLastPage
        )
    }

    fun getFollowerReviewByCafeIdAndUserId(cafeId: Long, loginMemberId: Long?): ReviewWithoutPagination {
        val followeeIds = loginMemberId?.let { memberFollowRepository.getFolloweeIds(loginMemberId) } ?: emptyList()
        val footprints = reviewRepository.findByCafeIdAndAuthorIdIn(cafeId, followeeIds)
        val reviewAndLikes =
            loginMemberId?.let { reviewRepository.findReviewsAndLikesOnThoseReviews(loginMemberId, footprints.mapNotNull { it.review?.id }) } ?: emptyMap()
        return ReviewWithoutPagination(
            reviews = footprints.map { footprint ->
                ReviewForCafeInfo(
                    id = footprint.review!!.id,
                    member = ReviewForCafeInfo.ReviewMemberInfo(footprint.member),
                    review = ReviewForCafeInfo.ReviewInfo(
                        id = footprint.review!!.id,
                        finalScore = footprint.review!!.finalScore.score,
                        images = footprint.review!!.images.map(ReviewForCafeInfo::ReviewImageInfo),
                        keywords = footprint.review!!.cafeKeywords.map(ReviewForCafeInfo::ReviewKeywordInfo),
                        likeCount = footprint.review!!.likeCount,
                        description = footprint.review!!.description,
                        isLiked = reviewAndLikes.getOrDefault(footprint.review!!.id, false),
                        isFolloweeReview = true,
                        commentCount = footprint.review!!.commentCount,
                        createdAt = footprint.createdAt
                    )
                )
            }
        )
    }
}
