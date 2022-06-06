package zip.cafe.entity

import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import zip.cafe.entity.Food.*

class FoodTest : FreeSpec({

    "생성" - {
        "문자열로 Food를 생성할 수 있다" {
            forAll(
                row("COFFEE", COFFEE),
                row("BEVERAGE", BEVERAGE),
                row("BAKERY", BAKERY),
                row("ETC", ETC)
            ) { str, food ->
                Food.from(str) shouldBe food
            }
        }
    }
})
