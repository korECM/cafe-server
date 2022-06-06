package zip.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.repository.CafeRepository
import zip.cafe.repository.MemberFollowRepository

@Transactional(readOnly = true)
@Service
class FeedService(
    private val cafeRepository: CafeRepository,
    private val memberFollowRepository: MemberFollowRepository
) {

    fun getReviewFeeds(loginMemberId: Long, minReviewIdInFeed: Long?) {
        val followeeIds = memberFollowRepository.getFolloweeIds(loginMemberId)
        cafeRepository.getReviewFeeds(loginMemberId, listOf(1, 2, 3), 5, minReviewIdInFeed)
    }
}
