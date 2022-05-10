package study.cafe.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import study.cafe.connector.S3Connector
import study.cafe.entity.Review
import study.cafe.entity.ReviewImage
import study.cafe.repository.*
import study.cafe.service.dto.ReviewRegisterDto
import study.cafe.service.dto.UploadedReviewImage

@Transactional(readOnly = true)
@Service
class ReviewService(
    private val memberRepository: MemberRepository,
    private val cafeRepository: CafeRepository,
    private val cafeKeywordRepository: CafeKeywordRepository,
    private val reviewRepository: ReviewRepository,
    private val reviewImageRepository: ReviewImageRepository,
    private val s3Connector: S3Connector,
    @Value("\${cloud.aws.s3.review-image-bucket}")
    private val reviewImageBucket: String
) {

    @Transactional
    fun createReview(cafeId: Long, uploadMemberId: Long, dto: ReviewRegisterDto) {
        val uploadMember = memberRepository.findOneById(uploadMemberId)
        val reviewImages = reviewImageRepository.findByIdIn(dto.reviewImageIds)
        require(reviewImages.size == dto.reviewImageIds.size) { "리뷰의 이미지 중 존재하지 않는 것이 있습니다" }
        reviewImages.forEach { it.checkIsUploadedBy(uploadMember) }

        val cafe = cafeRepository.findOneById(cafeId)
        val review = Review(cafe = cafe, member = uploadMember, finalScore = dto.finalScore, description = dto.description)

        review.addVisitPurposeInfo(dto.visitPurpose, dto.visitPurposeScore)

        // TODO 중복 체크
        dto.foodInfos.forEach { review.addFoodInfo(it.food, it.score) }

        cafeKeywordRepository.findByIdIn(dto.keywords).forEach(review::addCafeKeyword)

        reviewRepository.save(review)
        reviewImages.forEach { it.assignReview(review) }
    }

    @Transactional
    fun uploadReviewImages(uploadUserId: Long, images: List<MultipartFile>): List<UploadedReviewImage> {
        val uploadUser = memberRepository.findOneById(uploadUserId)
        val uploadReviewImages = images.map { s3Connector.uploadFile(bucketName = reviewImageBucket, dirName = "org", multipartFile = it) }
            .map {
                ReviewImage.createWithoutReview(
                    bucket = it.bucket,
                    fileKey = it.fileKey,
                    s3URL = it.s3URL,
                    cloudFrontURL = it.cloudFrontURL,
                    uploadedBy = uploadUser
                )
            }
        // TODO MaxUploadSizeExceededException 예외처리
        return uploadReviewImages.map {
            val savedReviewImage = reviewImageRepository.save(it)
            UploadedReviewImage.from(savedReviewImage)
        }
    }
}
