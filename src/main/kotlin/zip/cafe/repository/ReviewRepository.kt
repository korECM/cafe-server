package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import zip.cafe.entity.review.Review

fun ReviewRepository.findOneById(id: Long) = this.findByIdOrNull(id) ?: throw NoSuchElementException()

interface ReviewRepository : JpaRepository<Review, Long> {
    @Query("select distinct r from Review r inner join fetch r.cafe inner join fetch r.member left join fetch r._images left join fetch r._cafeKeywords ck left join fetch ck.cafeKeyword left join fetch r._likes where r.member.id in :authorIds")
    fun findByAuthorIdIn(authorIds: List<Long>): List<Review>
}
