package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import zip.cafe.entity.member.MemberFollow

interface MemberFollowRepository : JpaRepository<MemberFollow, Long> {
    @Query("select mf.to.id from MemberFollow mf where mf.from.id = :fromMemberId")
    fun getFolloweeIds(@Param("fromMemberId") fromMemberId: Long): List<Long>

    @Query("select (count(m) > 0) from MemberFollow m where m.from.id = :fromMemberId and m.to.id = :toMemberId")
    fun checkForFollowing(fromMemberId: Long, toMemberId: Long): Boolean
}
