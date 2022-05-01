package study.cafe.entity.member

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

    @OneToMany(mappedBy = "to", cascade = [ALL], orphanRemoval = true)
    val followers: MutableList<MemberFollow> = mutableListOf()

    @OneToMany(mappedBy = "from", cascade = [ALL], orphanRemoval = true)
    val followees: MutableList<MemberFollow> = mutableListOf()

    fun follow(member: Member) {
        println("followees = $followees")
        if (followees.any { memberFollow -> memberFollow.from == this }) {
            throw IllegalStateException("${member.id} 유저는 ${this.id} 유저가 이미 팔로우한 유저입니다")
        }
        val memberFollow = MemberFollow(from = this, to = member)
        followees += memberFollow
        member.followers += memberFollow
    }

    fun unfolow(member: Member) {
        val followMember = followees.find { memberFollow -> memberFollow.from == this }
        checkNotNull(followMember) { "${member.id} 유저는 ${this.id} 유저가 팔로우한 유저가 아닙니다" }
        followees -= followMember
        member.followers -= followMember
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
