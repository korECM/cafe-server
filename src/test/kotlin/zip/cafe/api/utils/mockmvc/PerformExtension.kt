package zip.cafe.api.utils.mockmvc

import org.springframework.restdocs.generate.RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE
import org.springframework.test.web.servlet.*

fun MockMvc.getWithPathParameter(urlTemplate: String, vararg vars: Any?, dsl: MockHttpServletRequestDsl.() -> Unit = {}): ResultActionsDsl =
    this.get(urlTemplate, *vars) {
        dsl(this)
        requestAttr(ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
    }

fun MockMvc.postWithPathParameter(urlTemplate: String, vararg vars: Any?, dsl: MockHttpServletRequestDsl.() -> Unit = {}): ResultActionsDsl =
    this.post(urlTemplate, *vars) {
        dsl(this)
        requestAttr(ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
    }

fun MockMvc.putWithPathParameter(urlTemplate: String, vararg vars: Any?, dsl: MockHttpServletRequestDsl.() -> Unit = {}): ResultActionsDsl =
    this.put(urlTemplate, *vars) {
        dsl(this)
        requestAttr(ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
    }

fun MockMvc.deleteWithPathParameter(urlTemplate: String, vararg vars: Any?, dsl: MockHttpServletRequestDsl.() -> Unit = {}): ResultActionsDsl =
    this.delete(urlTemplate, *vars) {
        dsl(this)
        requestAttr(ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
    }
