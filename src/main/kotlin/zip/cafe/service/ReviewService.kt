package zip.cafe.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation.NEVER
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import zip.cafe.connector.S3Connector
import zip.cafe.connector.dto.S3FileDto
import zip.cafe.entity.ReviewImage
import zip.cafe.entity.review.Footprint
import zip.cafe.entity.review.Review
import zip.cafe.repository.*
import zip.cafe.service.dto.ReviewRegisterDto
import java.time.LocalDate

@Transactional(readOnly = true)
@Service
class ReviewService(
    private val memberRepository: MemberRepository,
    private val cafeRepository: CafeRepository,
    private val cafeKeywordRepository: CafeKeywordRepository,
    private val reviewRepository: ReviewRepository,
    private val reviewImageRepository: ReviewImageRepository,
    private val footprintRepository: FootprintRepository,
    private val s3Connector: S3Connector,
    @Value("\${cloud.aws.s3.review-image-bucket}")
    private val reviewImageBucket: String
) {

    @Transactional
    fun createFootprintAndReview(cafeId: Long, memberId: Long, visitDate: LocalDate, dto: ReviewRegisterDto): Long {
        val footprintId = createFootprint(cafeId, memberId, visitDate)
        val reviewId = createReview(footprintId, memberId, dto)
        return reviewId
    }

    @Transactional
    fun createFootprint(cafeId: Long, memberId: Long, visitDate: LocalDate): Long {
        val member = memberRepository.findOneById(memberId)
        val cafe = cafeRepository.findOneById(cafeId)
        val footprint = Footprint(cafe = cafe, member = member, visitDate)
        footprintRepository.save(footprint)
        return footprint.id
    }

    @Transactional
    fun createReview(footprintId: Long, memberId: Long, dto: ReviewRegisterDto): Long {
        val footprint = footprintRepository.findOneById(footprintId)

        val uploadMember = memberRepository.findOneById(memberId)
        val reviewImages = reviewImageRepository.findByIdIn(dto.reviewImageIds)
        require(reviewImages.size == dto.reviewImageIds.size) { "리뷰의 이미지 중 존재하지 않는 것이 있습니다" }
        reviewImages.forEach { it.checkIsUploadedBy(uploadMember) }

        val review = Review.from(footprint = footprint, finalScore = dto.finalScore, description = dto.description)

        review.addVisitPurposeInfo(dto.visitPurpose, dto.visitPurposeScore)

        dto.foodInfos.forEach { review.addFoodInfo(it.food, it.score) }

        cafeKeywordRepository.findByIdIn(dto.keywords).forEach(review::addCafeKeyword)

        footprintRepository.save(footprint)
        reviewRepository.save(review)
        reviewImages.forEach { it.assignReview(review) }

        return review.id
    }

    @Transactional(propagation = NEVER)
    fun uploadReviewImages(images: List<MultipartFile>): List<S3FileDto> {
        // TODO MaxUploadSizeExceededException 예외처리
        return images.map { s3Connector.uploadFile(bucketName = reviewImageBucket, dirName = "org", multipartFile = it) }
    }

    @Transactional
    fun saveUploadedReviewImage(uploadUserId: Long, file: List<S3FileDto>): List<ReviewImage> {
        val uploadUser = memberRepository.findOneById(uploadUserId)
        val reviewImages = file.map {
            ReviewImage.createWithoutReview(
                bucket = it.bucket,
                fileKey = it.fileKey,
                s3URL = it.s3URL,
                cloudFrontURL = it.cloudFrontURL,
                uploadedBy = uploadUser
            )
        }
        return reviewImages.map { reviewImageRepository.save(it) }
    }
}
