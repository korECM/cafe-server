package zip.cafe.entity.member

import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.cafe.CafeLike
import zip.cafe.entity.common.BaseClass
import zip.cafe.util.logger
import java.time.LocalDate
import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.EnumType.STRING
import javax.persistence.GenerationType.IDENTITY

@Entity
class Member(
    @Column(nullable = true)
    val name: String,

    @Column(nullable = false)
    val nickname: String,

    @Column(nullable = false)
    var birthDay: LocalDate,

    @Enumerated(STRING)
    @Column(nullable = false)
    var gender: Gender
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id", nullable = false)
    val id: Long = 0

    @OneToMany(mappedBy = "member", cascade = [ALL], orphanRemoval = true)
    private val _likedCafes: MutableList<CafeLike> = mutableListOf()
    val likedCafes: List<CafeLike>
        get() = _likedCafes

    @OneToMany(mappedBy = "to", cascade = [ALL], orphanRemoval = true)
    private val _followers: MutableSet<MemberFollow> = mutableSetOf()
    val followers: List<Member>
        get() = _followers.map { it.from }

    @OneToMany(mappedBy = "from", cascade = [ALL], orphanRemoval = true)
    private val _followees: MutableSet<MemberFollow> = mutableSetOf()
    val followees: List<Member>
        get() = _followees.map { it.to }

    fun likeCafe(cafe: Cafe) {
        this._likedCafes += CafeLike(member = this, cafe = cafe)
        cafe.addLiker(this)
    }

    fun follow(member: Member) {
        val memberFollow = MemberFollow(from = this, to = member)
        _followees += memberFollow
        member._followers += memberFollow
    }

    fun unfolow(member: Member) {
        val followMember = _followees.find { memberFollow -> memberFollow.to == member }
        if (followMember == null) {
            logger().warn("${member.id} 유저는 ${this.id} 유저가 팔로우한 유저가 아닙니다")
            return
        }
        _followees -= followMember
        member._followers -= followMember
    }

    override fun toString(): String {
        return "Member(name='$name', nickname='$nickname', birthDay=$birthDay, gender=$gender, id=$id)"
    }
}
