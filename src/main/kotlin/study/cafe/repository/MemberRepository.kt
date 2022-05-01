package study.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import study.cafe.entity.member.Member

fun MemberRepository.findOneById(id: Long) = this.findByIdOrNull(id) ?: throw NoSuchElementException()

interface MemberRepository : JpaRepository<Member, Long> {
    fun existsByNicknameIs(nickname: String): Boolean
}
