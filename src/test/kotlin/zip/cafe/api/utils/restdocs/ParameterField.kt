package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import zip.cafe.api.utils.restdocs.RestDocs.DEFAULT_VALUE_KEY
import zip.cafe.api.utils.restdocs.RestDocs.EXAMPLE_KEY
import zip.cafe.api.utils.restdocs.RestDocs.FORMAT_KEY

class ParameterField(
    var type: ParameterDescriptor
) {
    init {
        type.attributes[DEFAULT_VALUE_KEY] = ""
        type.attributes[EXAMPLE_KEY] = ""
        type.attributes[FORMAT_KEY] = ""
    }

    infix fun example(value: String): ParameterField {
        type.attributes[EXAMPLE_KEY] = value
        return this
    }

    infix fun example(value: Number): ParameterField = example(value.toString())

    infix fun isOptional(value: Boolean): ParameterField {
        if (value) type = type.optional()
        return this
    }

    infix fun default(value: String): ParameterField {
        type.attributes[DEFAULT_VALUE_KEY] = value
        return this
    }

    infix fun default(value: Number): ParameterField = default(value.toString())

    infix fun format(value: String): ParameterField {
        type.attributes[FORMAT_KEY] = value
        return this
    }
}

infix fun String.means(mean: String) = ParameterField(parameterWithName(this).description(mean))
