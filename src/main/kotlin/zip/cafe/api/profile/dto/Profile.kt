package zip.cafe.api.profile.dto

import com.fasterxml.jackson.annotation.JsonFormat
import zip.cafe.config.defaultDateFormat
import zip.cafe.config.defaultDateTimeFormat
import zip.cafe.entity.ProfileImage
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.member.Member
import zip.cafe.entity.review.Footprint
import zip.cafe.entity.review.Review
import java.time.LocalDate
import java.time.LocalDateTime

data class CheckProfileResult(
    val memberId: Long,
    val isInit: Boolean,
    val nickname: String,
    val profileImageURL: String
) {
    companion object {
        fun from(member: Member) = CheckProfileResult(
            memberId = member.id,
            isInit = member.isProfileInit,
            nickname = member.nickname,
            profileImageURL = member.profileImage
        )
    }
}

data class ProfileInfo(
    val id: Long,
    val nickname: String,
    val profileImageURL: String,
    val description: String,
    val numberOfFootprint: Long,
    val numberOfReview: Long,
    val numberOfFollowers: Long,
    val numberOfFollowees: Long,
    val following: Boolean,
) {
    companion object {
        fun from(member: Member, isFollowing: Boolean) = ProfileInfo(
            id = member.id,
            nickname = member.nickname,
            profileImageURL = member.profileImage,
            description = member.description,
            numberOfFootprint = member.footprintCount,
            numberOfReview = member.reviewCount,
            numberOfFollowers = member.followeeCount,
            numberOfFollowees = member.followerCount,
            following = isFollowing,
        )
    }
}

data class ProfileFootprintInfo(
    val id: Long,
    val cafe: ProfileCafeInfo,
    @JsonFormat(pattern = defaultDateFormat)
    val visitDate: LocalDate,
    val reviewId: Long?,
) {
    companion object {
        fun from(footprint: Footprint) = ProfileFootprintInfo(
            id = footprint.id,
            cafe = ProfileCafeInfo.from(footprint.cafe),
            visitDate = footprint.visitDate,
            reviewId = footprint.review?.id
        )
    }
}

data class ProfileReviewInfo(
    val id: Long,
    val cafe: ProfileCafeInfo,
    val images: List<ProfileReviewImageInfo>,
    val finalScore: Double,
    val likeCount: Long,
    val description: String,
    val commentCount: Long,
    @JsonFormat(pattern = defaultDateTimeFormat)
    val createdAt: LocalDateTime
) {

    companion object {
        fun from(review: Review) = ProfileReviewInfo(
            id = review.id,
            cafe = ProfileCafeInfo.from(review.footprint.cafe),
            images = review.images.map { ProfileReviewImageInfo(it.id, it.cloudFrontURL) },
            finalScore = review.finalScore.score,
            likeCount = review.likeCount,
            description = review.description,
            commentCount = review.commentCount,
            createdAt = review.createdAt,
        )
    }
}

data class ProfileReviewImageInfo(
    val id: Long,
    val url: String
)

data class ProfileCafeInfo(
    val id: Long,
    val name: String,
    val address: String,
) {
    companion object {
        fun from(cafe: Cafe) = ProfileCafeInfo(
            id = cafe.id,
            name = cafe.name,
            address = cafe.address
        )
    }
}

data class UploadedProfileImageResponse(
    val id: Long,
    val url: String
) {
    companion object {
        fun from(image: ProfileImage): UploadedProfileImageResponse {
            return UploadedProfileImageResponse(
                id = image.id,
                url = image.cloudFrontURL
            )
        }
    }
}

data class InitProfileRequest(
    val nickname: String,
    val imageId: Long?,
)

class InitProfileResponse

data class EditProfileRequest(
    val nickname: String,
    val imageId: Long?,
)

class EditProfileResponse
