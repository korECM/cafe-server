package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.RequestFieldsSnippet

fun requestFields(vararg fields: Field): RequestFieldsSnippet = requestFields(fields.asList().map { it.type })
