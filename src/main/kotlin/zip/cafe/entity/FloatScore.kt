package zip.cafe.entity

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class FloatScore(
    @Column(nullable = false)
    val score: Double
) {
    init {
        if (!(1..10).map { it * 0.5 }.contains(score)) {
            throw IllegalArgumentException("점수는 0.5 ~ 5.0 사이의 숫자여야 합니다.")
        }
    }
}

fun Float.toScore() = FloatScore(this.toDouble())
fun Double.toScore() = FloatScore(this)
