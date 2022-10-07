package zip.cafe.entity.review

import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.common.BaseClass
import zip.cafe.entity.member.Member
import java.time.LocalDate
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
class Footprint(
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "cafe_id", nullable = false)
    val cafe: Cafe,

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    val visitDate: LocalDate,
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "footprint_id", nullable = false)
    val id: Long = 0

    @OneToOne(fetch = LAZY, optional = true)
    @JoinColumn(name = "review_id")
    var review: Review? = null
}
