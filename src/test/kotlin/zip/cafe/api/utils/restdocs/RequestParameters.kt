package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.restdocs.request.RequestParametersSnippet


fun requestParameters(vararg fields: ParameterField): RequestParametersSnippet = requestParameters(fields.asList().map { it.type })
