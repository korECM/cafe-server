package zip.cafe.seeds

import zip.cafe.entity.ReviewImage
import zip.cafe.entity.member.Member
import zip.cafe.entity.review.Review
import zip.cafe.utils.faker
import zip.cafe.utils.newEntityId
import zip.cafe.utils.setEntityId

fun createReviewImage(
    id: Long = faker.newEntityId(),
    bucket: String = faker.random.randomString(5, false),
    fileKey: String = faker.random.randomString(3, false),
    s3URL: String = faker.internet.domain(),
    cloudFrontURL: String = faker.internet.domain(),
    uploaderId: Long = faker.newEntityId(),
    uploadedBy: Member = createMember(id = uploaderId)
) = setEntityId(
    id,
    ReviewImage(
        bucket = bucket, fileKey = fileKey, s3URL = s3URL, cloudFrontURL = cloudFrontURL, uploadedBy = uploadedBy, review = null
    )
)

fun createReviewImageWithReview(
    id: Long = faker.newEntityId(),
    bucket: String = faker.random.randomString(5, false),
    fileKey: String = faker.random.randomString(3, false),
    s3URL: String = faker.internet.domain(),
    cloudFrontURL: String = faker.internet.domain(),
    uploaderId: Long = faker.newEntityId(),
    uploadedBy: Member = createMember(id = uploaderId),
    reviewId: Long = faker.newEntityId(),
    review: Review = createReview(id = reviewId)
) = setEntityId(
    id,
    ReviewImage(
        bucket = bucket, fileKey = fileKey, s3URL = s3URL, cloudFrontURL = cloudFrontURL, uploadedBy = uploadedBy, review = review
    )
)
