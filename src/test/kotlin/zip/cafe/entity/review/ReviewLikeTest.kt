package zip.cafe.entity.review

import io.kotest.core.spec.style.FreeSpec
import nl.jqno.equalsverifier.EqualsVerifier
import zip.cafe.entity.member.Member
import zip.cafe.seeds.createMember
import zip.cafe.seeds.createReview

class ReviewLikeTest : FreeSpec({

    "equalsAndHashCode" {
        EqualsVerifier.forClass(ReviewLike::class.java)
            .usingGetClass()
            .withOnlyTheseFields("member", "review")
            .withPrefabValues(Member::class.java, createMember(), createMember())
            .withPrefabValues(Review::class.java, createReview(), createReview())
            .verify()
    }
})
