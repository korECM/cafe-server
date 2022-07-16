package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param
import zip.cafe.entity.review.Review
import zip.cafe.service.dto.FollowerWhoWriteReview
import zip.cafe.service.dto.ReviewSummary

fun ReviewRepository.findOneById(id: Long) = this.findByIdOrNull(id) ?: throw NoSuchElementException()

interface ReviewRepository : JpaRepository<Review, Long>, ReviewRepositoryCustom {
    @Query("select new zip.cafe.service.dto.ReviewSummary(COUNT(r), COALESCE(AVG(r.finalScore), 0)) from Review r where r.cafe.id = :cafeId")
    fun getReviewSummaryByCafeId(@Param("cafeId") cafeId: Long): ReviewSummary

    @Query("select new zip.cafe.service.dto.FollowerWhoWriteReview(m.id, m.nickname) from Review r join r.member m where r.cafe.id = :cafeId and r.member.id in :memberIds")
    fun findWhoWriteReview(@Param("memberIds") memberIds: List<Long>, @Param("cafeId") cafeId: Long): List<FollowerWhoWriteReview>
}
