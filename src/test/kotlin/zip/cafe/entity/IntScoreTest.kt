package zip.cafe.entity

import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows

class IntScoreTest : FreeSpec({
    val validScoreList = listOf(1, 2, 3, 4, 5)

    "1~5 사이가 아니면 IllegalArgumentException을 던진다" {
        assertThrows<IllegalArgumentException> { IntScore(-3) }
        assertThrows<IllegalArgumentException> { IntScore(0) }
        assertThrows<IllegalArgumentException> { IntScore(6) }
    }
    "1부터 5사이면 그 숫자를 가지는 IntScore를 생성한다" - {
        withData(validScoreList) { score ->
            IntScore(score).score shouldBe score
        }
    }

    "확장 함수로 IntScore를 생성할 수 있다" {
        1.toScore() shouldBe IntScore(1)
        2.toScore() shouldBe IntScore(2)
        4.toScore() shouldBe IntScore(4)
    }
})
