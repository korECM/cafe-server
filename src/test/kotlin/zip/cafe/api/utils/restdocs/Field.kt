package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.snippet.Attributes.Attribute

class Field(
    var type: FieldDescriptor
) {
    infix fun means(description: String): Field {
        type = type.description(description)
        return this
    }

    infix fun example(example: String): Field {
        type = type.attributes(Attribute("example", example))
        return this
    }

    infix fun isOptional(value: Boolean): Field {
        if (value) type = type.optional()
        return this
    }
}

infix fun String.type(fieldType: DocsFieldType): Field {
    return Field(fieldWithPath(this).type(fieldType.type))
}
