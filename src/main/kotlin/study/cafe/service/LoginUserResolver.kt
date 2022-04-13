package study.cafe.service

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import study.cafe.entity.member.Member
import study.cafe.security.LoginFailedException
import study.cafe.security.LoginUser
import study.cafe.security.jwt.JwtTokenProvider

// TODO Resolver 등록하기
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
