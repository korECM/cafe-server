package zip.cafe.entity.cafe

import zip.cafe.entity.common.BaseClass
import zip.cafe.entity.review.CafeKeyword
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Entity
class CafeKeywordStat(
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "cafe_id")
    val cafe: Cafe,
    @ManyToOne(fetch = LAZY)
    val keyword: CafeKeyword,

    _count: Long,
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cafe_keyword_stat_id", nullable = false)
    val id: Long = 0

    @Column(nullable = false)
    var count: Long = _count
        protected set

    @Column(nullable = false)
    var rank: Long = 1

    fun incCount() {
        count++
    }
}
