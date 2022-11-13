package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import zip.cafe.entity.review.Review
import zip.cafe.service.dto.FollowerWhoWriteReview
import zip.cafe.service.dto.ReviewSummary

fun ReviewRepository.findOneById(id: Long) = this.findByIdOrNull(id) ?: throw NoSuchElementException()

interface ReviewRepository : JpaRepository<Review, Long>, ReviewRepositoryCustom {
    @Query("select distinct r from Review r left join fetch r._images join fetch r.footprint where r.id = :reviewId")
    fun getReviewDetailById(@Param("reviewId") reviewId: Long): Review?

    @Query("select new zip.cafe.service.dto.ReviewSummary(COUNT(r), COALESCE(AVG(r.finalScore), 0)) from Review r join r.footprint f where f.cafe.id = :cafeId")
    fun getReviewSummaryByCafeId(@Param("cafeId") cafeId: Long): ReviewSummary

    @Query("select new zip.cafe.service.dto.FollowerWhoWriteReview(m.id, m.nickname) from Review r join r.footprint f join f.member m where f.cafe.id = :cafeId and f.member.id in :memberIds")
    fun findWhoWriteReview(@Param("memberIds") memberIds: List<Long>, @Param("cafeId") cafeId: Long): List<FollowerWhoWriteReview>

    @Query("select r from Review r join fetch r.footprint where r.id = :reviewId")
    fun findByIdOrNull(@Param("reviewId") id: Long): Review?

    @Query("select r from Review r join fetch r.footprint where r.footprint.member.id = :memberId order by r.createdAt desc")
    fun findAllNewestReviewByMemberId(@Param("memberId") memberId: Long): List<Review>
}
