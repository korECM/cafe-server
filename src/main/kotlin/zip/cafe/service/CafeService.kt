package zip.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.repository.CafeRepository
import zip.cafe.repository.ReviewRepository
import zip.cafe.repository.findOneById

@Transactional(readOnly = true)
@Service
class CafeService(
    private val cafeRepository: CafeRepository,
    private val reviewRepository: ReviewRepository,
) {

    fun findById(id: Long) = cafeRepository.findOneById(id)

    fun findByIdForDetailPage(cafeId: Long) = cafeRepository.findOneByIdForDetail(cafeId)

    fun getReviewSummaryById(cafeId: Long) = reviewRepository.getReviewSummaryByCafeId(cafeId)

    fun getImageSummaryById(cafeId: Long) = cafeRepository.getImageSummaryByCafeId(cafeId)

    fun getKeywordSummaryById(cafeId: Long) = cafeRepository.getKeywordSummaryByCafeId(cafeId)
}
