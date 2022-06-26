package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.snippet.Snippet
import zip.cafe.utils.documentRequest
import zip.cafe.utils.documentResponse

fun document(
    identifier: String,
    vararg snippets: Snippet
): RestDocumentationResultHandler = MockMvcRestDocumentation.document(identifier, documentRequest, documentResponse, *snippets)


