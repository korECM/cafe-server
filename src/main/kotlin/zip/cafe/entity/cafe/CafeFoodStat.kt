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
    @Embedded
    val averageScore: AverageScore,
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cafe_food_stat_id", nullable = false)
    val id: Long = 0

    fun addScore(score: IntScore) {
        averageScore.add(score)
    }
}
