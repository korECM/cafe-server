package study.cafe.entity

import study.cafe.entity.common.BaseClass
import study.cafe.entity.member.Member
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
class ReviewImage(
    @Column(name = "bucket", nullable = false)
    val bucket: String,
    @Column(name = "fileKey", nullable = false)
    val fileKey: String,
    @Column(name = "s3URL", nullable = false)
    val s3URL: String,
    @Column(name = "cloudFrontURL", nullable = false)
    val cloudFrontURL: String,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val uploadedBy: Member,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    var review: Review?
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id", nullable = false)
    val id: Long = 0

    fun assignReview(review: Review) {
        this.review = review
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
