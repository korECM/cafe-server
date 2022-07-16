package zip.cafe.repository

import zip.cafe.entity.review.Review

interface ReviewRepositoryCustom {
    fun findByAuthorIdIn(authorIds: List<Long>): List<Review>
}
