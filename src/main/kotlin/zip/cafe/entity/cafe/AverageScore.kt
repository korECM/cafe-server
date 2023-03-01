package zip.cafe.entity.cafe

import zip.cafe.entity.IntScore
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class AverageScore(
    @Column(name = "count", nullable = false)
    var count: Long,
    @Column(name = "total_score", nullable = false)
    var total: Long
) {

    @Column(name = "average_score", nullable = false)
    var average: Double = if (count == 0L) 0.0 else total.toDouble() / count
        protected set

    fun add(score: IntScore) {
        total += score.score
        count += 1
        average = total.toDouble() / count
    }
}