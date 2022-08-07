package zip.cafe.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import zip.cafe.entity.member.Member
import zip.cafe.entity.member.QMember.member

@Repository
class SearchRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun searchByMemberNickname(query: String): List<Member> {
        return queryFactory
            .select(member)
            .from(member)
            .where(member.nickname.contains(query))
            .fetch()
    }
}
