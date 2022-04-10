package study.cafe.security

import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Component
import study.cafe.util.logger
import java.util.regex.Pattern

@Component
class BCryptPasswordEncoder : PasswordEncoder {
    private val BCRYPT_PATTERN = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}")
    private val logger = logger()

    private val salt: String
        get() = BCrypt.gensalt()

    override fun encode(rawPassword: CharSequence): String = BCrypt.hashpw(rawPassword.toString(), this.salt)

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        if (encodedPassword.isEmpty()) {
            logger.warn("Empty encoded password")
            return false
        }
        if (!BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
            logger.warn("Encoded password does not look like BCrypt")
            return false
        }
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword)
    }
}
