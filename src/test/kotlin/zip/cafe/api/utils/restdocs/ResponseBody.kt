package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.payload.PayloadDocumentation.responseFields

fun responseBody(vararg fields: Field) = responseFields(fields.asList().map { it.type })

fun responseBody(beneathPath: BeneathPath, vararg fields: Field) =
    responseFields(beneathPath.payloadSubsectionExtractor, fields.asList().map { it.type })
