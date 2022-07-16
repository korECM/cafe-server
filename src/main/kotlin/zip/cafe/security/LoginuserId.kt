package zip.cafe.security


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginUserId(
    val optional: Boolean = false
)
