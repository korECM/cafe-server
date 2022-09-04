package zip.cafe.service.auth.dto

import zip.cafe.entity.auth.LocalAuth
import zip.cafe.entity.member.Gender
import zip.cafe.entity.member.Member
import zip.cafe.entity.member.fromResidentRegistrationNumber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class LocalSignUpDto(
    val id: String,
    val password: String,
    val name: String,
    val nickname: String,
    val frontResidentRegistrationNumber: String,
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

    fun toLocalAuth(member: Member, encode: (String) -> String): LocalAuth {
        return LocalAuth(
            localId = id,
            localPassword = encode(password),
            member = member
        )
    }
}

data class LocalSignInDto(
    val id: String,
    val password: String
)
