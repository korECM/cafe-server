package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import zip.cafe.entity.review.Review

fun ReviewRepository.findOneById(id: Long) = this.findByIdOrNull(id) ?: throw NoSuchElementException()

interface ReviewRepository : JpaRepository<Review, Long> {
    @Query("select r from Review r where r.member.id in :authorIds")
    fun findByAuthorIdIn(authorIds: List<Long>): List<Review>
}
