package zip.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.entity.Food
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.member.Member
import zip.cafe.entity.review.Purpose
import zip.cafe.entity.review.ReviewCafeKeyword
import zip.cafe.repository.SearchRepository

@Transactional(readOnly = true)
@Service
class SearchService(
    private val searchRepository: SearchRepository
) {

    fun searchMember(name: String): List<Member> = searchRepository.searchByMemberNickname(name)
    fun searchCafe(name: String, visitPurposeList: List<Purpose>, foodList: List<Food>, keywordIdList: List<Long>): List<Cafe> =
        searchRepository.searchCafe(name, 3, visitPurposeList, 3, foodList, 3, keywordIdList)

    fun searchKeyword(keyword: String): List<ReviewCafeKeyword> = searchRepository.searchByKeyword(keyword)
}
