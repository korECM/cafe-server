package zip.cafe.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import zip.cafe.entity.review.Footprint
import zip.cafe.entity.review.QFootprint.footprint
import zip.cafe.entity.review.QReview.review
import zip.cafe.entity.review.QReviewLike.reviewLike

class ReviewRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ReviewRepositoryCustom {
    override fun findByAuthorIdIn(authorIds: List<Long>, minReviewIdInFeed: Long?, limit: Long): List<Footprint> {
        return queryFactory
            .select(footprint)
            .from(footprint)
            .innerJoin(footprint.review, review).fetchJoin()
            .innerJoin(footprint.member).fetchJoin()
            .innerJoin(footprint.cafe).fetchJoin()
            .where(footprint.member.id.`in`(authorIds).and(olderThanHasEverSeen(minReviewIdInFeed)))
            .orderBy(footprint.id.desc())
            .limit(limit)
            .fetch()
    }

    private fun olderThanHasEverSeen(minReviewIdInFeed: Long?): BooleanExpression? = minReviewIdInFeed?.let { review.id.lt(it) }

    override fun isLastPageByAuthorIds(authorIds: List<Long>, minReviewIdInFeed: Long, limit: Long): Boolean {
        // limit보다 1 크게 조회해서 조회되는 게시글 개수 비교
        return queryFactory
            .select(review)
            .from(review)
            .innerJoin(review.footprint, footprint)
            .where(footprint.member.id.`in`(authorIds).and(olderThanHasEverSeen(minReviewIdInFeed)))
            .orderBy(review.id.desc())
            .limit(limit + 1)
            .fetch().size <= limit
    }

    override fun findByCafeId(cafeId: Long, minReviewIdInCafeDetail: Long?, limit: Long): List<Footprint> {
        return queryFactory
            .select(footprint)
            .from(footprint)
            .innerJoin(footprint.review, review).fetchJoin()
            .innerJoin(footprint.member).fetchJoin()
            .innerJoin(footprint.cafe).fetchJoin()
            .where(footprint.cafe.id.eq(cafeId).and(olderThanHasEverSeen(minReviewIdInCafeDetail)))
            .orderBy(footprint.id.desc())
            .limit(limit)
            .fetch()
    }

    override fun findByCafeIdAndAuthorIdIn(cafeId: Long, authorIds: List<Long>): List<Footprint> {
        return queryFactory
            .select(footprint)
            .from(footprint)
            .innerJoin(footprint.review, review).fetchJoin()
            .innerJoin(footprint.member).fetchJoin()
            .innerJoin(footprint.cafe).fetchJoin()
            .where(footprint.cafe.id.eq(cafeId).and(footprint.member.id.`in`(authorIds)))
            .orderBy(footprint.id.desc())
            .fetch()
    }

    override fun isLastPageByCafeId(cafeId: Long, minReviewIdInFeed: Long, limit: Long): Boolean {
        // limit보다 1 크게 조회해서 조회되는 게시글 개수 비교
        return queryFactory
            .select(review)
            .from(review)
            .innerJoin(review.footprint, footprint)
            .where(footprint.cafe.id.eq(cafeId).and(olderThanHasEverSeen(minReviewIdInFeed)))
            .orderBy(review.id.desc())
            .limit(limit + 1)
            .fetch().size <= limit
    }

    override fun findReviewsAndLikesOnThoseReviews(authorId: Long, reviewIds: List<Long>): Map<Long, Boolean> {
        val likedReviewId = queryFactory
            .select(reviewLike.review.id)
            .from(reviewLike)
            .where(reviewLike.review.id.`in`(reviewIds).and(reviewLike.member.id.eq(authorId)))
            .fetch()
        return reviewIds.associateWith { likedReviewId.contains(it) }
    }
}
