package zip.cafe.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import zip.cafe.entity.review.QFootprint.footprint
import zip.cafe.entity.review.QReview.review
import zip.cafe.entity.review.Review

class ReviewRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ReviewRepositoryCustom {
    override fun findByAuthorIdIn(authorIds: List<Long>, minReviewIdInFeed: Long?, limit: Long): List<Review> {
        return queryFactory
            .select(review)
            .distinct()
            .from(review)
            .innerJoin(review.footprint, footprint).fetchJoin()
            .where(footprint.member.id.`in`(authorIds).and(olderThanHasEverSeen(minReviewIdInFeed)))
            .orderBy(review.id.desc())
            .limit(limit)
            .fetch()
    }

    private fun olderThanHasEverSeen(minReviewIdInFeed: Long?): BooleanExpression? = minReviewIdInFeed?.let { review.id.lt(it) }

    override fun isLastPage(authorIds: List<Long>, minReviewIdInFeed: Long, limit: Long): Boolean {
        // limit보다 1 크게 조회해서 조회되는 게시글 개수 비교
        return queryFactory
            .select(review.id.count())
            .from(review)
            .innerJoin(review.footprint, footprint)
            .where(footprint.member.id.`in`(authorIds).and(olderThanHasEverSeen(minReviewIdInFeed)))
            .orderBy(review.id.desc())
            .limit(limit + 1)
            .fetchOne()?.let { it <= limit } ?: true
    }
}
