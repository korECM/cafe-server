package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.snippet.Attributes.Attribute

class PathParameterField(
    var type: ParameterDescriptor
) {
    infix fun example(example: String): PathParameterField {
        type = type.attributes(Attribute("example", example))
        return this
    }

    infix fun isOptional(value: Boolean): PathParameterField {
        if (value) type = type.optional()
        return this
    }
}

infix fun String.means(mean: String) = PathParameterField(parameterWithName(this).description(mean))
