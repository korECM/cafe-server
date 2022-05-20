package study.cafe.entity.member

import study.cafe.entity.Cafe
import study.cafe.entity.CafeLike
import study.cafe.entity.common.BaseClass
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
    private val _followers: MutableList<MemberFollow> = mutableListOf()
    val followers: List<MemberFollow>
        get() = _followers

    @OneToMany(mappedBy = "from", cascade = [ALL], orphanRemoval = true)
    private val _followees: MutableList<MemberFollow> = mutableListOf()
    val followees: List<MemberFollow>
        get() = _followees

    fun likeCafe(cafe: Cafe) {
        this._likedCafes += CafeLike(member = this, cafe = cafe)
        cafe.addLiker(this)
    }

    fun follow(member: Member) {
        println("followees = $followees")
        if (followees.any { memberFollow -> memberFollow.from == this }) {
            throw IllegalStateException("${member.id} 유저는 ${this.id} 유저가 이미 팔로우한 유저입니다")
        }
        val memberFollow = MemberFollow(from = this, to = member)
        _followees += memberFollow
        member._followers += memberFollow
    }

    fun unfolow(member: Member) {
        val followMember = followees.find { memberFollow -> memberFollow.from == this }
        checkNotNull(followMember) { "${member.id} 유저는 ${this.id} 유저가 팔로우한 유저가 아닙니다" }
        _followees -= followMember
        member._followers -= followMember
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Member
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
