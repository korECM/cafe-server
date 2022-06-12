package zip.cafe.repository

import com.querydsl.core.types.dsl.Expressions.constant
import com.querydsl.jpa.impl.JPAQueryFactory
import zip.cafe.entity.QReviewImage.reviewImage
import zip.cafe.entity.cafe.QCafe.cafe
import zip.cafe.entity.member.QMember.member
import zip.cafe.entity.review.QCafeKeyword.cafeKeyword
import zip.cafe.entity.review.QReview.review
import zip.cafe.entity.review.QReviewCafeKeyword.reviewCafeKeyword
import zip.cafe.service.dto.QReviewFeed
import zip.cafe.service.dto.ReviewFeed

class CafeRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CafeRepositoryCustom {

    override fun getReviewFeeds(loginMemberId: Long, followeeIds: List<Long>, size: Long, minReviewIdInFeed: Long?): List<ReviewFeed> {
//        return listOf()
        return queryFactory.select(
            QReviewFeed(
                cafe.id,
                cafe.name,
                cafe.address,
                member.id,
                member.name,
                review.finalScore.score,
                constant(0),
                constant(0),
                review.createdAt,
                reviewImage.id,
                reviewImage.cloudFrontURL,
                cafeKeyword.id,
                cafeKeyword.keyword,
                cafeKeyword.emoji
            )
        )
            .from(review)
            .join(review.cafe, cafe)
            .join(review.member, member)
            .leftJoin(review._images, reviewImage)
            .leftJoin(review._cafeKeywords, reviewCafeKeyword)
            .leftJoin(reviewCafeKeyword.cafeKeyword, cafeKeyword)
            .where(review.id.`in`(followeeIds), minReviewLt(minReviewIdInFeed))
            .limit(size)
            .fetch()
    }

    private fun minReviewLt(minReviewIdInFeed: Long?) = minReviewIdInFeed?.let { review.id.lt(it) }
}
