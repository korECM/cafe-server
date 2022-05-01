package study.cafe.service

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import study.cafe.security.LoginFailedException
import study.cafe.security.LoginUserId
import study.cafe.security.jwt.JwtTokenProvider

@Component
class LoginUserIdResolver(
    private val jwtTokenProvider: JwtTokenProvider
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginUserId::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Long {
        val token = extractToken(webRequest)
        if (!jwtTokenProvider.validateToken(token)) {
            throw LoginFailedException()
        }
        return jwtTokenProvider.getUserPk(token)
    }

    private fun extractToken(request: NativeWebRequest): String {
        return request.getHeader("x-access-token") ?: throw LoginFailedException()
    }
}
