package zip.cafe.entity.member

import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows
import zip.cafe.entity.member.Gender.FEMALE
import zip.cafe.entity.member.Gender.MALE

class GenderTest : FreeSpec({

    "생성" - {
        "from" - {
            "문자열로 enum을 생성할 수 있다" {
                forAll(
                    row("male", MALE),
                    row("MALE", MALE),
                    row("female", FEMALE),
                    row("FEMALE", FEMALE),
                ) { str, gender ->
                    Gender.from(str) shouldBe gender
                }
            }

            "유효하지 않은 문자열로 생성하려고 하면 IllegalArgumentException을 던진다" {
                assertThrows<IllegalArgumentException> { Gender.from("asdf") }
                assertThrows<IllegalArgumentException> { Gender.from("") }
                assertThrows<IllegalArgumentException> { Gender.from("123") }
            }
        }

        "fromResidentRegistrationNumber" - {
            "주민번호 앞자리로 생성할 수 있다" {
                forAll(
                    row(0, FEMALE),
                    row(1, MALE),
                    row(2, FEMALE),
                    row(3, MALE),
                    row(4, FEMALE),
                    row(5, MALE),
                    row(6, FEMALE),
                    row(7, MALE),
                    row(8, FEMALE),
                    row(9, MALE),
                ) { firstDigit, gender ->
                    Gender.fromResidentRegistrationNumber(firstDigit) shouldBe gender
                }
            }

            "유효하지 않은 숫자가 들어오면 IllegalArgumentException을 던진다" {
                assertThrows<IllegalArgumentException> { Gender.fromResidentRegistrationNumber(13) }
                assertThrows<IllegalArgumentException> { Gender.fromResidentRegistrationNumber(-1) }
            }
        }
    }
})
