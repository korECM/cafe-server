package zip.cafe.entity.review

import io.kotest.core.spec.style.FreeSpec
import nl.jqno.equalsverifier.EqualsVerifier
import zip.cafe.seeds.createReview

class ReviewCafeKeywordTest : FreeSpec({

    "equals and hashcode" {
        EqualsVerifier.forClass(ReviewCafeKeyword::class.java)
            .usingGetClass()
            .withOnlyTheseFields("cafeKeyword", "review")
            .withPrefabValues(Review::class.java, createReview(), createReview())
            .verify()
    }
})
