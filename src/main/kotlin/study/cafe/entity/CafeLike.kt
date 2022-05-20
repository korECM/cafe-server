package study.cafe.entity

import study.cafe.entity.common.BaseClass
import study.cafe.entity.member.Member
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Entity
class CafeLike(
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    val cafe: Cafe,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cafe_like_id", nullable = false)
    val id: Long = 0
}
