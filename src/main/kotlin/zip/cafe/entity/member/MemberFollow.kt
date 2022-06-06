package zip.cafe.entity.member

import zip.cafe.entity.common.BaseClass
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
        // NOTE from, to가 불변이니까 아래 값에 의존해서 equals와 hashCode를 작성할 수 있음
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberFollow

        if (from != other.from) return false
        if (to != other.to) return false

        return true
    }

    override fun hashCode(): Int {
        // NOTE from, to가 불변이니까 아래 값에 의존해서 equals와 hashCode를 작성할 수 있음
        var result = from.hashCode()
        result = 31 * result + to.hashCode()
        return result
    }
}
