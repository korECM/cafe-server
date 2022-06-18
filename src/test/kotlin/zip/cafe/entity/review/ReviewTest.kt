package zip.cafe.entity.review

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import zip.cafe.seeds.createMember
import zip.cafe.seeds.createReview

class ReviewTest : FreeSpec({

    "리뷰 좋아요" - {
        "멤버가 이 리뷰를 좋아한다" {
            // given
            val member1 = createMember()
            val member2 = createMember()
            val review = createReview()
            // when
            review.addLiker(member1)
            review.addLiker(member2)
            // then
            review.likers shouldContainExactly listOf(member1, member2)
        }

        "이미 좋아요를 한 경우 중복으로 추가되지 않는다" {
            // given
            val member = createMember()
            val review = createReview()
            // when
            review.addLiker(member)
            review.addLiker(member)
            // then
            review.likers shouldHaveSize 1
            review.likers shouldContainExactly listOf(member)
        }
    }
})
