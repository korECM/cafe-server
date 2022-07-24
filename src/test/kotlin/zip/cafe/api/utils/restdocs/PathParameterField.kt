package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import zip.cafe.api.utils.restdocs.RestDocs.DEFAULT_VALUE_KEY
import zip.cafe.api.utils.restdocs.RestDocs.EXAMPLE_KEY
import zip.cafe.api.utils.restdocs.RestDocs.FORMAT_KEY

class PathParameterField(
    var type: ParameterDescriptor
) {
    init {
        type.attributes[DEFAULT_VALUE_KEY] = ""
        type.attributes[EXAMPLE_KEY] = ""
        type.attributes[FORMAT_KEY] = ""
    }

    infix fun example(example: String): PathParameterField {
        type.attributes[EXAMPLE_KEY] = example
        return this
    }

    infix fun isOptional(value: Boolean): PathParameterField {
        if (value) type = type.optional()
        return this
    }

    infix fun default(value: String): PathParameterField {
        type.attributes[DEFAULT_VALUE_KEY] = value
        return this
    }

    infix fun format(value: String): PathParameterField {
        type.attributes[FORMAT_KEY] = value
        return this
    }
}

infix fun String.means(mean: String) = PathParameterField(parameterWithName(this).description(mean))
