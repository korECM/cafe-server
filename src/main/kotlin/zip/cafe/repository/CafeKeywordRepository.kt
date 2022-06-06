package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import zip.cafe.entity.review.CafeKeyword

fun CafeKeywordRepository.findOneById(id: Long) = this.findByIdOrNull(id) ?: throw NoSuchElementException()

interface CafeKeywordRepository : JpaRepository<CafeKeyword, Long> {
    fun findByIdIn(ids: List<Long>): List<CafeKeyword>
}
