package zip.cafe.api.dto

import io.swagger.v3.oas.annotations.media.Schema
import zip.cafe.service.dto.LocalSignInDto
import zip.cafe.service.dto.LocalSignUpDto
import javax.validation.constraints.*

data class LocalSignUpRequest(
    @Schema(title = "ID", example = "testId")
    @field:NotBlank(message = "아이디는 공백이 될 수 없습니다")
    val id: String,

    @Schema(title = "Password", example = "1234")
    @field:NotBlank(message = "패스워드는 공백이 될 수 없습니다")
    val password: String,

    @Schema(title = "이름", example = "홍길동", required = false)
    @field:NotNull
    @field:Size(min = 2, max = 10)
    val name: String,

    @Schema(title = "닉네임", example = "길동이")
    @field:NotBlank(message = "닉네임은 공백이 될 수 없습니다")
    @field:Size(min = 3, max = 10)
    val nickname: String,

    @Schema(title = "주민번호 앞자리", example = "000102")
    @field:NotBlank(message = "주민번호 앞자리는 공백이 될 수 없습니다")
    @field:Size(min = 6, max = 6)
    val frontResidentRegistrationNumber: String,

    @Schema(title = "주민번호 뒷자리 첫 번째 숫자", example = "1")
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
    @Schema(title = "아이디", example = "testId")
    @field:NotBlank(message = "아이디는 공백이 될 수 없습니다")
    val id: String,
    @Schema(title = "패스워드", example = "1234")
    @field:NotBlank(message = "패스워드는 공백이 될 수 없습니다")
    val password: String
) {
    fun toDto(): LocalSignInDto = LocalSignInDto(id = id, password = password)
}

data class LocalSignInResponse(
    val token: String
)