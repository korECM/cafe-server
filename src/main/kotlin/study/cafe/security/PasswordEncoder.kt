package study.cafe.security

interface PasswordEncoder {
    fun encode(rawPassword: CharSequence): String
    fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean
}
