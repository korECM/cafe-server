package zip.cafe.repository

import zip.cafe.service.dto.ReviewFeed

interface CafeRepositoryCustom {
    fun getReviewFeeds(loginMemberId: Long, followeeIds: List<Long>, size: Long, minReviewIdInFeed: Long?): List<ReviewFeed>
}
