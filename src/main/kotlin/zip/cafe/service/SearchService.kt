package zip.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.entity.member.Member
import zip.cafe.repository.SearchRepository

@Transactional(readOnly = true)
@Service
class SearchService(
    private val searchRepository: SearchRepository
) {

    fun search(query: String): List<Member> = searchRepository.searchByMemberNickname(query)
}
