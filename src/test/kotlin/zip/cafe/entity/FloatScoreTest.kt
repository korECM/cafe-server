package zip.cafe.entity

import io.kotest.core.spec.style.FreeSpec
import org.junit.jupiter.api.assertThrows

class FloatScoreTest : FreeSpec({
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
})
