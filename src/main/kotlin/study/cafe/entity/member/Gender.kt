package study.cafe.entity.member

enum class Gender {
    MALE, FEMALE;

    companion object
}

fun Gender.Companion.fromResidentRegistrationNumber(firstDigit: Number) = when (firstDigit) {
    1, 3, 5, 7, 9 -> Gender.MALE
    0, 2, 4, 6, 8 -> Gender.FEMALE
    else -> throw IllegalArgumentException("$firstDigit 은 유효하지 않은 주민번호 뒷자리 첫 번째 숫자입니다")
}
