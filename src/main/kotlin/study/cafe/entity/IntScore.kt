package study.cafe.entity

import javax.persistence.Embeddable

@Embeddable
data class IntScore(
    val score: Int
) {
    init {
        if (!(1..5).contains(score)) {
            throw IllegalArgumentException("${score}는 올바른 만족도 점수가 아닙니다")
        }
    }
}
