package study.cafe.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import study.cafe.service.LoginUserIdResolver
import study.cafe.service.LoginUserResolver

@Configuration
class AuthenticationConfig(
    private val loginUserResolver: LoginUserResolver,
    private val loginUserIdResolver: LoginUserIdResolver
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginUserResolver)
        resolvers.add(loginUserIdResolver)
    }
}
