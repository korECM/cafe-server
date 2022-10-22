package zip.cafe.entity

import zip.cafe.entity.member.Member
import zip.cafe.entity.review.Review
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Table(name = "review_image")
@Entity
class ReviewImage(
    bucket: String,
    fileKey: String,
    s3URL: String,
    cloudFrontURL: String,
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    val uploadedBy: Member,
    @ManyToOne(fetch = LAZY, optional = true)
    @JoinColumn(name = "review_id")
    var review: Review?
) : Image(bucket, fileKey, s3URL, cloudFrontURL) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id", nullable = false)
    val id: Long = 0

    fun assignReview(review: Review) {
        this.review = review
        review.addImage(this)
    }

    fun checkIsUploadedBy(user: Member) {
        require(uploadedBy == user) { "이 리뷰 이미지[$id]는 사용자[${user.id}]에 의해 업로드된 이미지가 아닙니다" }
    }

    companion object {
        fun createWithoutReview(bucket: String, fileKey: String, s3URL: String, cloudFrontURL: String, uploadedBy: Member): ReviewImage {
            return ReviewImage(
                bucket = bucket,
                fileKey = fileKey,
                s3URL = s3URL,
                cloudFrontURL = cloudFrontURL,
                uploadedBy = uploadedBy,
                review = null
            )
        }
    }
}
