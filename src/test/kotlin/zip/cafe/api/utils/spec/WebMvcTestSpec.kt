package zip.cafe.api.utils.spec

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.extensions.spring.SpringExtension
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.slot
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.core.MethodParameter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.filter.CharacterEncodingFilter
import zip.cafe.security.LoginFailedException
import zip.cafe.security.LoginUser
import zip.cafe.security.LoginUserId
import zip.cafe.seeds.MOCK_MVC_USER_ID
import zip.cafe.seeds.createMember
import zip.cafe.service.resolver.LoginUserIdResolver
import zip.cafe.service.resolver.LoginUserResolver

@ExtendWith(RestDocumentationExtension::class)
@AutoConfigureRestDocs
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@WebAppConfiguration
@Suppress("SpringJavaAutowiredMembersInspection")
open class WebMvcTestSpec(body: FreeSpec.() -> Unit = {}) : FreeSpec(body) {

    override fun extensions() = listOf(SpringExtension)

    @MockkBean
    private lateinit var loginUserResolver: LoginUserResolver

    @MockkBean
    private lateinit var loginUserIdResolver: LoginUserIdResolver

    @Autowired
    private lateinit var restDocumentationContextProvider: RestDocumentationContextProvider

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    lateinit var objectMapper: ObjectMapper

    protected lateinit var mockMvc: MockMvc

    override suspend fun beforeTest(testCase: TestCase) {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .apply<DefaultMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider))
            .build()

        loginUserResolver.also {
            slot<MethodParameter>().also { slot ->
                every { it.supportsParameter(capture(slot)) } answers {
                    slot.captured.hasParameterAnnotation(LoginUser::class.java)
                }
            }
            slot<NativeWebRequest>().also { slot ->
                every { it.resolveArgument(any(), any(), capture(slot), any()) } answers {
                    val hasToken = slot.captured.getHeader("x-access-token") == null
                    if (!hasToken) {
                        throw LoginFailedException()
                    }
                    createMember(id = MOCK_MVC_USER_ID)
                }
            }
        }
        loginUserIdResolver.also {
            slot<MethodParameter>().also { slot ->
                every { it.supportsParameter(capture(slot)) } answers {
                    slot.captured.hasParameterAnnotation(LoginUserId::class.java)
                }
            }
            slot<NativeWebRequest>().also { slot ->
                every { it.resolveArgument(any(), any(), capture(slot), any()) } answers {
                    val hasToken = slot.captured.getHeader("x-access-token") == null
                    if (!hasToken) {
                        throw LoginFailedException()
                    }
                    MOCK_MVC_USER_ID
                }
            }
        }
    }
}
