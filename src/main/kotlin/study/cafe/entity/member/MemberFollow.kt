package study.cafe.entity.member

import study.cafe.entity.common.BaseClass
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
class MemberFollow(
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "from_member_id", nullable = false)
    val from: Member,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "to_member_id", nullable = false)
    val to: Member
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_follow_id", nullable = false)
    val id: Long = 0
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MemberFollow
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
