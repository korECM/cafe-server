package zip.cafe.api.dto

import zip.cafe.service.auth.dto.LocalSignInDto
import zip.cafe.service.auth.dto.LocalSignUpDto
import javax.validation.constraints.*

data class LocalSignUpRequest(
    @field:NotBlank(message = "아이디는 공백이 될 수 없습니다")
    val id: String,

    @field:NotBlank(message = "패스워드는 공백이 될 수 없습니다")
    val password: String,

    @field:NotNull
    @field:Size(min = 2, max = 10)
    val name: String,

    @field:NotBlank(message = "닉네임은 공백이 될 수 없습니다")
    @field:Size(min = 3, max = 10)
    val nickname: String,

    @field:NotBlank(message = "주민번호 앞자리는 공백이 될 수 없습니다")
    @field:Size(min = 6, max = 6)
    val frontResidentRegistrationNumber: String,

    @field:Min(0, message = "올바른 주민번호 뒷자리의 첫 번째 숫자가 아닙니다")
    @field:Max(9, message = "올바른 주민번호 뒷자리의 첫 번째 숫자가 아닙니다")
    val seventhDigitOfResidentRegistrationNumber: Number,

    ) {
    fun toDto(): LocalSignUpDto = LocalSignUpDto(
        id = id,
        password = password,
        name = name,
        nickname = nickname,
        frontResidentRegistrationNumber = frontResidentRegistrationNumber,
        seventhDigitOfResidentRegistrationNumber = seventhDigitOfResidentRegistrationNumber
    )
}

data class LocalSignUpResponse(
    val token: String
)

data class LocalSignInRequest(
    @field:NotBlank(message = "아이디는 공백이 될 수 없습니다")
    val id: String,
    @field:NotBlank(message = "패스워드는 공백이 될 수 없습니다")
    val password: String
) {
    fun toDto(): LocalSignInDto = LocalSignInDto(id = id, password = password)
}

data class LocalSignInResponse(
    val token: String
)
