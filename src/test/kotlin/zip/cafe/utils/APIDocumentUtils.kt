package zip.cafe.utils

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors.*

val documentRequest: OperationRequestPreprocessor = preprocessRequest(
    modifyUris()
        .scheme("https")
        .host("docs.api.com")
        .removePort(),
    prettyPrint()
)

val documentResponse: OperationResponsePreprocessor = preprocessResponse(prettyPrint())
