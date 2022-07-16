package zip.cafe.repository

import zip.cafe.entity.review.Review

interface ReviewRepositoryCustom {
    fun findByAuthorIdIn(authorIds: List<Long>, minReviewIdInFeed: Long?, limit: Long): List<Review>
}
