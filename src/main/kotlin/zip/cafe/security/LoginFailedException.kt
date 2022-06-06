package zip.cafe.security

class LoginFailedException(message: String = "로그인 정보에 문제가 있습니다") : RuntimeException(message)
