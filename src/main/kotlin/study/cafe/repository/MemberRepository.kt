package study.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import study.cafe.entity.Member

interface MemberRepository : JpaRepository<Member, Long> {
    fun existsByNicknameIs(nickname: String): Boolean
}
