package zip.cafe.entity.cafe

import zip.cafe.entity.IntScore
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Transient

@Embeddable
data class AverageScore(
    @Column(name = "count", nullable = false)
    val count: Long,
    @Column(name = "total_score", nullable = false)
    var total: Long
) {

    @get:Transient
    val average: Double
        get() = if (count == 0L) 0.0 else total.toDouble() / count

    fun add(score: IntScore) {
        total += score.score
    }
}