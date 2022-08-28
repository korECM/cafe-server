package zip.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.member.Member
import zip.cafe.repository.SearchRepository

@Transactional(readOnly = true)
@Service
class SearchService(
    private val searchRepository: SearchRepository
) {

    fun searchMember(name: String): List<Member> = searchRepository.searchByMemberNickname(name)
    fun searchCafe(name: String): List<Cafe> = searchRepository.searchByCafeName(name)
}
