package zip.cafe.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import zip.cafe.service.resolver.LoginUserIdResolver
import zip.cafe.service.resolver.LoginUserResolver

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
