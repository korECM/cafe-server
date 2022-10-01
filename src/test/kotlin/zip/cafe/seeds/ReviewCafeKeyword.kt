package zip.cafe.seeds

import zip.cafe.entity.review.CafeKeyword
import zip.cafe.entity.review.Review
import zip.cafe.entity.review.ReviewCafeKeyword
import zip.cafe.utils.faker
import zip.cafe.utils.newEntityId
import zip.cafe.utils.setEntityId

fun createReviewCafeKeyword(
    id: Long = faker.newEntityId(),
    cafeKeywordId: Long = faker.newEntityId(),
    cafeKeyword: CafeKeyword = createCafeKeyword(id = cafeKeywordId),
    reviewId: Long = faker.newEntityId(),
    review: Review = createReview(id = reviewId)
) = setEntityId(
    id,
    ReviewCafeKeyword(
        cafeKeyword = cafeKeyword,
        review = review
    )
)
