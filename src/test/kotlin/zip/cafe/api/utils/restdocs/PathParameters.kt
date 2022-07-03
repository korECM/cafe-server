package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.request.PathParametersSnippet
import org.springframework.restdocs.request.RequestDocumentation.pathParameters

fun pathParameters(vararg fields: PathParameterField): PathParametersSnippet = pathParameters(fields.asList().map { it.type })
