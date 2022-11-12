package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import zip.cafe.entity.cafe.CafeKeywordStat

interface CafeKeywordStatRepository : JpaRepository<CafeKeywordStat, Long> {
    fun findAllByCafeId(cafeId: Long): List<CafeKeywordStat>
}
