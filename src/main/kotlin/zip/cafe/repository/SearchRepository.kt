package zip.cafe.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.cafe.QCafe.cafe
import zip.cafe.entity.member.Member
import zip.cafe.entity.member.QMember.member

@Repository
class SearchRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun searchByMemberNickname(query: String): List<Member> = queryFactory
        .select(member)
        .from(member)
        .where(member.nickname.contains(query))
        .fetch()

    fun searchByCafeName(name: String): List<Cafe> = queryFactory
        .select(cafe)
        .from(cafe)
        .where(cafe.name.contains(name))
        .fetch()
}
