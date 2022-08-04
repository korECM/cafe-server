package zip.cafe.api.utils.mockmvc

import org.springframework.restdocs.generate.RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE
import org.springframework.test.web.servlet.*

fun MockMvc.getWithPathParameter(urlTemplate: String, vararg vars: Any?): ResultActionsDsl =
    this.get(urlTemplate, *vars) {
        requestAttr(ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
    }

fun MockMvc.postWithPathParameter(urlTemplate: String, vararg vars: Any?): ResultActionsDsl =
    this.post(urlTemplate, *vars) {
        requestAttr(ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
    }

fun MockMvc.putWithPathParameter(urlTemplate: String, vararg vars: Any?): ResultActionsDsl =
    this.put(urlTemplate, *vars) {
        requestAttr(ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
    }

fun MockMvc.deleteWithPathParameter(urlTemplate: String, vararg vars: Any?): ResultActionsDsl =
    this.delete(urlTemplate, *vars) {
        requestAttr(ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
    }
