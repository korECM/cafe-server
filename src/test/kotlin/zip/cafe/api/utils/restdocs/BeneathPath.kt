package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.payload.PayloadDocumentation.beneathPath
import org.springframework.restdocs.payload.PayloadSubsectionExtractor

class BeneathPath(
    var payloadSubsectionExtractor: PayloadSubsectionExtractor<*>
)

infix fun String.beneathPathWithSubsectionId(subsectionId: String): BeneathPath = BeneathPath(beneathPath(this).withSubsectionId(subsectionId))
