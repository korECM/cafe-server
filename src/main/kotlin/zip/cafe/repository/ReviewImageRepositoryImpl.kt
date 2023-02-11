package zip.cafe.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import zip.cafe.entity.QReviewImage.reviewImage

class ReviewImageRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ReviewImageRepositoryCustom {

    override fun findReviewImageUploaderIdsNumberOfReviewImagesIsGreaterThanExceptMembers(memberIds: List<Long>, numberOfReviewImagesThreshold: Long): List<Long> {
        return queryFactory
            .select(reviewImage.uploadedBy.id)
            .from(reviewImage)
            .where(reviewImage.uploadedBy.id.notIn(memberIds))
            .groupBy(reviewImage.uploadedBy.id)
            .having(reviewImage.uploadedBy.id.count().goe(numberOfReviewImagesThreshold))
            .fetch()
    }
}
