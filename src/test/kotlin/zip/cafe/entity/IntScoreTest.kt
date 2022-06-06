package zip.cafe.entity

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows

class IntScoreTest : FreeSpec({
    "1~5 사이가 아니면 IllegalArgumentException을 던진다" {
        assertThrows<IllegalArgumentException> { IntScore(-3) }
        assertThrows<IllegalArgumentException> { IntScore(0) }
        assertThrows<IllegalArgumentException> { IntScore(6) }
    }
    "1부터 5사이면 그 숫자를 가지는 IntScore를 생성한다" {
        // given
        val score1 = IntScore(1)
        val score2 = IntScore(3)
        val score3 = IntScore(5)
        // when
        // then
        score1.score shouldBe 1
        score2.score shouldBe 3
        score3.score shouldBe 5
    }

    "확장 함수로 IntScore를 생성할 수 있다" {
        1.toScore() shouldBe IntScore(1)
        2.toScore() shouldBe IntScore(2)
        4.toScore() shouldBe IntScore(4)
    }
})
