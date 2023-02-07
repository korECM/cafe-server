package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import zip.cafe.entity.member.Member

fun MemberRepository.findOneById(id: Long) = this.findByIdOrNull(id) ?: throw NoSuchElementException()

interface MemberRepository : JpaRepository<Member, Long> {
    fun existsByNicknameIs(nickname: String): Boolean

    @Query("select m from Member m join fetch m._followees join fetch m._followers where m.id = :id")
    fun findOneByIdWithFollower(id: Long): Member?
}
