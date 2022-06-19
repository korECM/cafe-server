package zip.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.ReviewRepository
import zip.cafe.repository.findOneById

@Transactional(readOnly = true)
@Service
class ReviewLikeService(
    private val reviewRepository: ReviewRepository,
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun likeReview(memberId: Long, reviewId: Long) {
        val member = memberRepository.findOneById(memberId)
        val review = reviewRepository.findOneById(reviewId)

        review.addLiker(member)
    }
}
