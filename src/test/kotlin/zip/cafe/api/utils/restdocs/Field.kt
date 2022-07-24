package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import zip.cafe.api.utils.restdocs.RestDocs.DEFAULT_VALUE_KEY
import zip.cafe.api.utils.restdocs.RestDocs.EXAMPLE_KEY
import zip.cafe.api.utils.restdocs.RestDocs.FORMAT_KEY

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

    infix fun example(example: String): Field {
        type.attributes[RestDocs.EXAMPLE_KEY] = example
        return this
    }

    infix fun isOptional(value: Boolean): Field {
        if (value) type = type.optional()
        return this
    }

    infix fun default(value: String): Field {
        type.attributes[DEFAULT_VALUE_KEY] = value
        return this
    }

    infix fun formattedAs(value: String): Field {
        type.attributes[RestDocs.FORMAT_KEY] = value
        return this
    }
}

infix fun String.type(fieldType: DocsFieldType): Field {
    if (fieldType is ENUM<*>) {
        return Field(fieldWithPath(this).type(fieldType.type)).example(fieldType.enums.joinToString(" or "))
    }
    return Field(fieldWithPath(this).type(fieldType.type))
}
