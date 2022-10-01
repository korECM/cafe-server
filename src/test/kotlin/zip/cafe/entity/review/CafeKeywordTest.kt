package zip.cafe.entity.review

import io.kotest.core.spec.style.FreeSpec
import nl.jqno.equalsverifier.EqualsVerifier

class CafeKeywordTest : FreeSpec({
    "equalsAndHashCode" {
        EqualsVerifier.forClass(CafeKeyword::class.java)
            .usingGetClass()
            .withOnlyTheseFields("keyword", "emoji")
            .verify()
    }
})
