package study.cafe.entity

import javax.persistence.Embeddable

@Embeddable
data class FloatScore(
    val score: Double
) {
    init {
        if (!(1..10).map { it * 0.5 }.contains(score)) {
            throw IllegalArgumentException("점수는 0.5 ~ 5.0 사이의 숫자여야 합니다.")
        }
    }
}
