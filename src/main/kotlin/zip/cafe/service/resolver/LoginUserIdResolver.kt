package zip.cafe.service.resolver

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import zip.cafe.security.LoginFailedException
import zip.cafe.security.LoginUserId
import zip.cafe.security.jwt.JwtTokenProvider

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
    ): Long? = try {
        val token = extractToken(webRequest)
        println(token)
        if (jwtTokenProvider.isInvalidToken(token)) {
            throw LoginFailedException()
        }
        jwtTokenProvider.getUserPk(token)
    } catch (e: Exception) {
        println(e)
        val annotation = parameter.getParameterAnnotation(LoginUserId::class.java)!!
        if (annotation.optional) null else throw e
    }

    private fun extractToken(request: NativeWebRequest): String {
        return request.getHeader("x-access-token") ?: throw LoginFailedException()
    }
}
