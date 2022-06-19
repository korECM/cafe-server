package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields

fun responseBody(vararg descriptors: FieldDescriptor) = responseFields(descriptors.asList())

fun responseBody(vararg fields: Field) = responseFields(fields.asList().map { it.type })
