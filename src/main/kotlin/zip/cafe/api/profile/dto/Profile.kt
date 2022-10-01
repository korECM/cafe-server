package zip.cafe.api.profile.dto

import zip.cafe.entity.member.Member

data class ProfileInfo(
    val id: Long,
    val nickname: String,
    val profileImageURL: String,
    val description: String,
    val sumOfReviewAndFootPrint : Int,
    val numberOfFollowers: Int,
    val numberOfFollowees: Int
) {
    companion object {
        fun from(member: Member) = ProfileInfo(
            id = member.id,
            nickname = member.nickname,
            profileImageURL = member.profileImage,
            description = "",
            sumOfReviewAndFootPrint = 0,
            numberOfFollowers = member.followers.size,
            numberOfFollowees = member.followees.size
        )
    }
}
