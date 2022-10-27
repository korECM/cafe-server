package zip.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param
import zip.cafe.entity.review.Footprint

fun FootprintRepository.findOneById(id: Long) = this.findByIdOrNull(id) ?: throw NoSuchElementException()

interface FootprintRepository : JpaRepository<Footprint, Long> {
    @Query("select f from Footprint f where f.member.id = :memberId order by f.createdAt desc")
    fun findAllNewestFootprintByMemberId(@Param("memberId") memberId: Long): List<Footprint>
}
