package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import zip.cafe.api.utils.restdocs.RestDocs.DEFAULT_VALUE_KEY
import zip.cafe.api.utils.restdocs.RestDocs.EXAMPLE_KEY
import zip.cafe.api.utils.restdocs.RestDocs.FORMAT_KEY
import zip.cafe.config.defaultDateFormat
import zip.cafe.config.defaultDateTimeFormat

class Field(
    var type: FieldDescriptor
) {
    init {
        type.attributes[DEFAULT_VALUE_KEY] = ""
        type.attributes[EXAMPLE_KEY] = ""
        type.attributes[FORMAT_KEY] = ""
    }

    infix fun means(description: String): Field {
        type = type.description(description)
        return this
    }

    infix fun example(value: String): Field {
        type.attributes[EXAMPLE_KEY] = value
        return this
    }

    infix fun example(value: Number): Field = example(value.toString())

    infix fun isOptional(value: Boolean): Field {
        if (value) type = type.optional()
        return this
    }

    infix fun default(value: String): Field {
        type.attributes[DEFAULT_VALUE_KEY] = value
        return this
    }

    infix fun default(value: Number): Field = default(value.toString())

    infix fun formattedAs(value: String): Field {
        type.attributes[FORMAT_KEY] = value
        return this
    }
}

infix fun String.type(fieldType: DocsFieldType): Field = when (fieldType) {
    is ENUM<*> -> Field(fieldWithPath(this).type(fieldType.type)).example(fieldType.enums.joinToString(" or "))
    is DATETIME -> Field(fieldWithPath(this).type(fieldType.type)).formattedAs(defaultDateTimeFormat)
    is DATE -> Field(fieldWithPath(this).type(fieldType.type)).formattedAs(defaultDateFormat)
    else -> Field(fieldWithPath(this).type(fieldType.type))
}
