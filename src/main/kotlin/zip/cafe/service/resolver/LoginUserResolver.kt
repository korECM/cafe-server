package zip.cafe.service.resolver

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import zip.cafe.entity.member.Member
import zip.cafe.security.LoginFailedException
import zip.cafe.security.LoginUser
import zip.cafe.security.jwt.JwtTokenProvider
import zip.cafe.service.MemberService

@Component
class LoginUserResolver(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberService: MemberService
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Member {
        val token = extractToken(webRequest)
        if (!jwtTokenProvider.validateToken(token)) {
            throw LoginFailedException()
        }
        val userPkId = jwtTokenProvider.getUserPk(token)
        return memberService.findMemberById(userPkId)
    }

    private fun extractToken(request: NativeWebRequest): String {
        return request.getHeader("x-access-token") ?: throw LoginFailedException()
    }
}
