package zip.cafe.api.profile.dto

import zip.cafe.entity.member.Member

data class ProfileInfo(
    val id: Long,
    val nickname: String,
    val profileImageURL: String,
    val description: String,
    val numberOfFollowers: Int,
    val numberOfFollowees: Int
) {
    companion object {
        fun from(member: Member) = ProfileInfo(
            id = member.id,
            nickname = member.nickname,
            profileImageURL = member.profileImage,
            description = "",
            numberOfFollowers = member.followers.size,
            numberOfFollowees = member.followees.size
        )
    }
}
