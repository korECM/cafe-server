package zip.cafe.entity.member

import zip.cafe.entity.common.BaseClass
import zip.cafe.util.logger
import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.GenerationType.IDENTITY

@Entity
class Member(
    @Column(nullable = false)
    val nickname: String,

    @Column(nullable = false)
    val profileImage: String,

    @Column(nullable = false)
    val description : String = "",

    @Column(nullable = false)
    val isProfileInit : Boolean = false,

    _followerCount: Long = 0L,
    _followeeCount: Long = 0L,
    _footprintCount: Long = 0L,
    _reviewCount: Long = 0L,
) : BaseClass() {
    constructor(nickname: String) : this(nickname, DEFAULT_PROFILE_IMAGE_URL)

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id", nullable = false)
    val id: Long = 0

    @OneToMany(mappedBy = "to", cascade = [ALL], orphanRemoval = true)
    private val _followers: MutableSet<MemberFollow> = mutableSetOf()
    val followers: List<Member>
        get() = _followers.map { it.from }

    @OneToMany(mappedBy = "from", cascade = [ALL], orphanRemoval = true)
    private val _followees: MutableSet<MemberFollow> = mutableSetOf()
    val followees: List<Member>
        get() = _followees.map { it.to }

    @Column(nullable = false)
    var followerCount: Long = _followerCount
        protected set

    @Column(nullable = false)
    var followeeCount: Long = _followeeCount
        protected set

    @Column(nullable = false)
    var reviewCount: Long = _reviewCount

    @Column(nullable = false)
    var footprintCount: Long = _footprintCount

    fun follow(member: Member) {
        val memberFollow = MemberFollow(from = this, to = member)
        val isNewFollowee = _followees.add(memberFollow)
        if (isNewFollowee) {
            followeeCount += 1
            member._followers += memberFollow
            member.followerCount += 1
        }
    }

    fun unfolow(member: Member) {
        val followMember = _followees.find { memberFollow -> memberFollow.to == member }
        if (followMember == null) {
            logger().warn("${member.id} 유저는 ${this.id} 유저가 팔로우한 유저가 아닙니다")
            return
        }
        _followees -= followMember
        followeeCount -= 1
        member._followers -= followMember
        member.followerCount -= 1
    }

    override fun toString(): String {
        return "Member(nickname='$nickname', id=$id)"
    }

    companion object {
        const val DEFAULT_PROFILE_IMAGE_URL = "https://picsum.photos/200"
    }
}
