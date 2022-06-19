package zip.cafe.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.ReviewRepository
import zip.cafe.seeds.createMember
import zip.cafe.seeds.createReview

class ReviewLikeServiceTest : FreeSpec({

    val memberRepository: MemberRepository = mockk(relaxed = true)
    val reviewRepository: ReviewRepository = mockk(relaxed = true)
    val reviewLikeService = ReviewLikeService(
        reviewRepository = reviewRepository, memberRepository = memberRepository
    )

    "리뷰 좋아요" - {
        "멤버가 리뷰에 좋아요를 누른다" {
            // given
            val memberId = 1L
            val member = createMember(id = memberId)
            val reviewId = 3L
            val review = createReview(id = reviewId)
            // mock
            every { memberRepository.findByIdOrNull(memberId) } returns member
            every { reviewRepository.findByIdOrNull(reviewId) } returns review
            // when
            reviewLikeService.likeReview(memberId, reviewId)
            // then
            review.likers shouldContainExactly listOf(member)
        }

        "멤버가 이미 리뷰에 좋아요를 눌렀더라도 좋아요 기록은 1개만 남는다" {
            // given
            val memberId = 1L
            val member = createMember(id = memberId)
            val reviewId = 3L
            val review = createReview(id = reviewId)
            // mock
            every { memberRepository.findByIdOrNull(memberId) } returns member
            every { reviewRepository.findByIdOrNull(reviewId) } returns review
            // when
            reviewLikeService.likeReview(memberId, reviewId)
            reviewLikeService.likeReview(memberId, reviewId)
            // then
            review.likers shouldContainExactly listOf(member)
        }
    }

    "리뷰 좋아요 취소" - {
        "멤버가 좋아했던 리뷰의 좋아요를 취소한다" {
            // given
            val member = createMember(id = 1L)
            val review = createReview(id = 3L)
            // mock
            every { memberRepository.findByIdOrNull(member.id) } returns member
            every { reviewRepository.findByIdOrNull(review.id) } returns review
            // when
            reviewLikeService.likeReview(member.id, review.id)
            reviewLikeService.cancelLikeReview(member.id, review.id)
            // then
            review.likers shouldHaveSize 0
        }

        "멤버가 좋아하지 않았던 리뷰의 좋아요를 취소한다" {
            // given
            val member1 = createMember(id = 1L)
            val member2 = createMember(id = 3L)
            val review = createReview(id = 5L)
            // mock
            every { memberRepository.findByIdOrNull(member1.id) } returns member1
            every { memberRepository.findByIdOrNull(member2.id) } returns member2
            every { reviewRepository.findByIdOrNull(review.id) } returns review
            // when
            reviewLikeService.likeReview(member2.id, review.id)
            reviewLikeService.cancelLikeReview(member1.id, review.id)
            // then
            review.likers shouldContainExactly listOf(member2)
        }
    }

    afterTest { clearMocks(memberRepository, reviewRepository) }
})
