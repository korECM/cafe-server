package zip.cafe.entity

import zip.cafe.entity.member.Member
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Table(name = "profile_image")
@Entity
class ProfileImage(
    bucket: String,
    fileKey: String,
    s3URL: String,
    cloudFrontURL: String,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    var member: Member?,
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "upload_member_id", nullable = false)
    val uploadedBy: Member,
) : Image(bucket, fileKey, s3URL, cloudFrontURL) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_image_id", nullable = false)
    val id: Long = 0

    companion object {
        fun from(bucket: String, fileKey: String, s3URL: String, cloudFrontURL: String, uploadedBy: Member, member : Member): ProfileImage {
            return ProfileImage(
                bucket = bucket,
                fileKey = fileKey,
                s3URL = s3URL,
                cloudFrontURL = cloudFrontURL,
                uploadedBy = uploadedBy,
                member = uploadedBy,
            )
        }

        fun withoutMember(bucket: String, fileKey: String, s3URL: String, cloudFrontURL: String, uploadedBy: Member): ProfileImage {
            return ProfileImage(
                bucket = bucket,
                fileKey = fileKey,
                s3URL = s3URL,
                cloudFrontURL = cloudFrontURL,
                uploadedBy = uploadedBy,
                member = null
            )
        }
    }
}
