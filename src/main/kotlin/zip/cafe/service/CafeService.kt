package zip.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
}
