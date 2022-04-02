package study.cafe.api.dto.local

import org.hibernate.validator.constraints.Length
import study.cafe.entity.Gender
import study.cafe.entity.Member
import study.cafe.entity.auth.LocalAuth
import study.cafe.entity.fromResidentRegistrationNumber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.validation.constraints.*

data class LocalSignUpRequest(
    @field:NotBlank
    val id: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    @field:Pattern(regexp = "[0-9]{3}[-]+[0-9]{3,4}[-]+[0-9]{4}")
    val phoneNumber: String,

    @field:NotNull
    @field:Length(min = 2, max = 10)
    val name: String,

    @field:NotBlank
    @field:Length(min = 3, max = 10)
    val nickname: String,

    @field:NotBlank
    @field:Length(min = 6, max = 6)
    val frontResidentRegistrationNumber: String,

    @field:Min(1)
    @field:Max(7)
    val seventhDigitOfResidentRegistrationNumber: Number,

) {
    fun toMember(): Member {
        val birthDay = LocalDate.parse("20$frontResidentRegistrationNumber", DateTimeFormatter.BASIC_ISO_DATE)
        val gender = Gender.fromResidentRegistrationNumber(seventhDigitOfResidentRegistrationNumber)
        return Member(
            name = name,
            nickname = nickname,
            birthDay = birthDay,
            gender = gender
        )
    }

    fun toLocalAuth(member: Member): LocalAuth {
        return LocalAuth(
            localId = id,
            localPassword = password,
            phoneNumber = phoneNumber,
            member = member
        )
    }
}
