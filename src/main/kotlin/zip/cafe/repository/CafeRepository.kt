package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param
import zip.cafe.entity.ReviewImage
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.cafe.CafeKeywordStat
import zip.cafe.service.dto.FollowerWhoLikeCafe

fun CafeRepository.findOneById(id: Long) = this.findByIdOrNull(id) ?: throw NoSuchElementException()

interface CafeRepository : JpaRepository<Cafe, Long> {

    @Query("select c from Cafe c left join fetch c._menus m join fetch m.menu where c.id = :id")
    fun findOneByIdForDetail(@Param("id") id: Long): Cafe?

    @Query("select distinct i from ReviewImage i join i.review r join r.footprint f where f.cafe.id = :cafeId")
    fun getImageSummaryByCafeId(@Param("cafeId") cafeId: Long): List<ReviewImage>

    @Query("select cs from CafeKeywordStat cs join fetch cs.keyword where cs.cafe.id = :cafeId")
    fun getKeywordSummaryByCafeId(@Param("cafeId") cafeId: Long): List<CafeKeywordStat>

    @Query("select new zip.cafe.service.dto.FollowerWhoLikeCafe(m.id, m.nickname) from Cafe c inner join CafeLike cl on c.id = cl.cafe.id join cl.member m where c.id = :cafeId and cl.member.id in :memberIds")
    fun findWhoLikeCafe(@Param("memberIds") memberIds: List<Long>, @Param("cafeId") cafeId: Long): List<FollowerWhoLikeCafe>
}
