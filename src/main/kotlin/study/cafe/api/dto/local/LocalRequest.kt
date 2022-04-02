package study.cafe.api.dto.local

import io.swagger.v3.oas.annotations.media.Schema
import study.cafe.entity.Gender
import study.cafe.entity.Member
import study.cafe.entity.auth.LocalAuth
import study.cafe.entity.fromResidentRegistrationNumber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class LocalSignUpRequest(
    @Schema(title = "ID", example = "testId")
    @field:NotBlank
    val id: String,

    @Schema(title = "Password", example = "1234")
    @field:NotBlank
    val password: String,

    @Schema(title = "핸드폰 번호", example = "010-1234-1234")
    @field:NotBlank
    @field:Pattern(regexp = "[0-9]{3}[-]+[0-9]{3,4}[-]+[0-9]{4}")
    val phoneNumber: String,

    @Schema(title = "이름", example = "홍길동", required = false)
    @field:NotNull
    @field:Size(min = 2, max = 10)
    val name: String,

    @Schema(title = "닉네임", example = "길동이")
    @field:NotBlank
    @field:Size(min = 3, max = 10)
    val nickname: String,

    @Schema(title = "주민번호 앞자리", example = "000102")
    @field:NotBlank
    @field:Size(min = 6, max = 6)
    val frontResidentRegistrationNumber: String,

    @Schema(title = "주민번호 뒷자리 첫 번째 숫자", example = "1")
    @field:Size(min = 1, max = 1)
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
