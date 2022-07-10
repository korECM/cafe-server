package zip.cafe.entity.cafe

import zip.cafe.entity.common.BaseClass
import zip.cafe.entity.member.Member
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Table(name= "cafe_like")
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
