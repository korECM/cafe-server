package zip.cafe.api.utils.mockmvc

import org.springframework.restdocs.generate.RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get

fun MockMvc.getWithPathParameter(urlTemplate: String, vararg vars: Any?): ResultActionsDsl =
    this.get(urlTemplate, *vars) {
        requestAttr(ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
    }
