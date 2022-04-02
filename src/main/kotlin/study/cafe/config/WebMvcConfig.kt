package study.cafe.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CharacterEncodingFilter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebMvcConfig : WebMvcConfigurer {
    @Bean
    fun characterEncodingFilter(): CharacterEncodingFilter {
        val characterEncodingFilter = CharacterEncodingFilter()
        characterEncodingFilter.encoding = "UTF-8"
        characterEncodingFilter.setForceEncoding(true)
        return characterEncodingFilter
    }
}
