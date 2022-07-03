package zip.cafe.entity.review

import io.kotest.core.spec.style.FreeSpec
import nl.jqno.equalsverifier.EqualsVerifier
import zip.cafe.entity.member.Member
import zip.cafe.seeds.createMember
import zip.cafe.seeds.createReview

class ReviewFoodInfoTest : FreeSpec({

    "equalsAndHashCode" {
        EqualsVerifier.forClass(ReviewFoodInfo::class.java)
            .usingGetClass()
            .withOnlyTheseFields("food", "review")
            .withPrefabValues(Member::class.java, createMember(), createMember())
            .withPrefabValues(Review::class.java, createReview(), createReview())
            .verify()
    }
})
