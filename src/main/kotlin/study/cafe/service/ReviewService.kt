package study.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import study.cafe.entity.Review
import study.cafe.repository.CafeKeywordRepository
import study.cafe.repository.ReviewRepository
import study.cafe.service.dto.ReviewRegisterDto

@Transactional(readOnly = true)
@Service
class ReviewService(
    private val cafeKeywordRepository: CafeKeywordRepository,
    private val reviewRepository: ReviewRepository
) {

    @Transactional
    fun createReview(dto: ReviewRegisterDto) {
        val review = Review(cafe = dto.cafe, member = dto.member, finalScore = dto.finalScore, description = dto.description)

        review.addVisitPurposeInfo(dto.visitPurpose, dto.visitPurposeScore)
        // TODO 중복 체크
        dto.foodInfos.forEach { info -> review.addFoodInfo(info.food, info.score) }

        cafeKeywordRepository.findByIdIn(dto.keywords).forEach(review::addCafeKeyword)

        reviewRepository.save(review)
    }
}
