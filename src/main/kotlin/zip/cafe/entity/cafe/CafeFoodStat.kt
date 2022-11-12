package zip.cafe.entity.cafe

import zip.cafe.entity.Food
import zip.cafe.entity.IntScore
import zip.cafe.entity.common.BaseClass
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Entity
class CafeFoodStat(
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "cafe_id")
    val cafe: Cafe,
    @Enumerated(EnumType.STRING)
    val food: Food,
    @Column(nullable = false)
    val count: Long,
    @Column(nullable = false)
    var totalScore: Long,
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cafe_food_stat_id", nullable = false)
    val id: Long = 0

    var averageScore = totalScore / count
        protected set

    fun addScore(score: IntScore) {
        totalScore += score.score
        averageScore = totalScore / count
    }
}
