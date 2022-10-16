package zip.cafe.repository

import zip.cafe.entity.review.Footprint

interface ReviewRepositoryCustom {
    fun findByAuthorIdIn(authorIds: List<Long>, minReviewIdInFeed: Long?, limit: Long): List<Footprint>
    fun isLastPage(authorIds: List<Long>, minReviewIdInFeed: Long, limit: Long): Boolean
}
