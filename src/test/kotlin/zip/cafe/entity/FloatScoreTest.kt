package zip.cafe.entity

import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows

class FloatScoreTest : FreeSpec({
    val validScoreList = listOf(0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0)

    "점수가 0.5보다 작거나 5보다 크다면 IllegalArgumentException을 던진다" {
        assertThrows<IllegalArgumentException> { FloatScore(-3.0) }
        assertThrows<IllegalArgumentException> { FloatScore(0.3) }
        assertThrows<IllegalArgumentException> { FloatScore(0.1) }
        assertThrows<IllegalArgumentException> { FloatScore(0.0) }
        assertThrows<IllegalArgumentException> { FloatScore(5.1) }
        assertThrows<IllegalArgumentException> { FloatScore(5.5) }
    }

    "점수가 0.5 단위가 아니면 IllegalArgumentException을 던진다" {
        assertThrows<IllegalArgumentException> { FloatScore(4.3) }
        assertThrows<IllegalArgumentException> { FloatScore(1.3) }
    }

    "0.5부터 5.0 사이라면 그 숫자를 가지는 FloatScore를 생성한다" - {
        withData(validScoreList) { score ->
            FloatScore(score).score shouldBe score
        }
    }

    "확장 함수로 FloatScore를 생성할 수 있다" {
        0.5.toScore() shouldBe FloatScore(0.5)
        1.5f.toScore() shouldBe FloatScore(1.5)
    }
})
