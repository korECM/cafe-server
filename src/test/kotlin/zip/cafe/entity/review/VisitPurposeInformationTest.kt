package zip.cafe.entity.review

import io.kotest.core.spec.style.FreeSpec
import nl.jqno.equalsverifier.EqualsVerifier
import zip.cafe.seeds.createReview

class VisitPurposeInformationTest : FreeSpec({

    "equalsAndHashCode" {
        EqualsVerifier.forClass(VisitPurposeInformation::class.java)
            .usingGetClass()
            .withOnlyTheseFields("visitPurpose", "review")
            .withPrefabValues(Review::class.java, createReview(), createReview())
            .verify()
    }
})
