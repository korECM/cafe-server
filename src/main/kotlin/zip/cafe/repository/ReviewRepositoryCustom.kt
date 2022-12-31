package zip.cafe.repository

import zip.cafe.entity.review.Footprint

interface ReviewRepositoryCustom {
    fun findByAuthorIdIn(authorIds: List<Long>, minReviewIdInFeed: Long?, limit: Long): List<Footprint>
    fun isLastPageByAuthorIds(authorIds: List<Long>, minReviewIdInFeed: Long, limit: Long): Boolean

    fun findByCafeId(cafeId: Long, minReviewIdInCafeDetail: Long?, limit: Long): List<Footprint>
    fun findByCafeIdAndAuthorIdIn(cafeId: Long, authorIds: List<Long>): List<Footprint>
    fun isLastPageByCafeId(cafeId: Long, minReviewIdInFeed: Long, limit: Long): Boolean

    fun findReviewsAndLikesOnThoseReviews(authorId: Long, reviewIds: List<Long>): Map<Long, Boolean>
}
