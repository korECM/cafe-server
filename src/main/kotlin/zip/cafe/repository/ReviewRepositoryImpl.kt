package zip.cafe.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import zip.cafe.entity.review.QReview.review
import zip.cafe.entity.review.QReviewCafeKeyword.reviewCafeKeyword
import zip.cafe.entity.review.Review

class ReviewRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ReviewRepositoryCustom {
    override fun findByAuthorIdIn(authorIds: List<Long>): List<Review> {
        return queryFactory
            .select(review)
            .from(review)
            .innerJoin(review.cafe).fetchJoin()
            .innerJoin(review.member).fetchJoin()
            .leftJoin(review._images).fetchJoin()
            .leftJoin(review._cafeKeywords, reviewCafeKeyword).fetchJoin()
            .leftJoin(reviewCafeKeyword.cafeKeyword).fetchJoin()
            .leftJoin(review._likes).fetchJoin()
            .where(review.member.id.`in`(authorIds))
            .fetch()
    }
}
