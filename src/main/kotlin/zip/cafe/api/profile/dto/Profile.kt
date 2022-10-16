package zip.cafe.api.profile.dto

import zip.cafe.entity.member.Member

data class ProfileInfo(
    val id: Long,
    val nickname: String,
    val profileImageURL: String,
    val description: String,
    val numberOfFootprint : Long,
    val numberOfReview : Long,
    val numberOfFollowers: Long,
    val numberOfFollowees: Long
) {
    companion object {
        fun from(member: Member) = ProfileInfo(
            id = member.id,
            nickname = member.nickname,
            profileImageURL = member.profileImage,
            description = member.description,
            numberOfFootprint = member.footprintCount,
            numberOfReview = member.reviewCount,
            numberOfFollowers = member.followeeCount,
            numberOfFollowees = member.followerCount
        )
    }
}
