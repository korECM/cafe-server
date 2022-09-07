package zip.cafe.api.utils.mockmvc

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.web.servlet.MockMvcResultHandlersDsl
import zip.cafe.utils.documentRequest
import zip.cafe.utils.documentResponse

fun MockMvcResultHandlersDsl.documentWithHandle(
    identifier: String,
    vararg snippets: Snippet
): RestDocumentationResultHandler = MockMvcRestDocumentation.document(identifier, documentRequest, documentResponse, *snippets)
