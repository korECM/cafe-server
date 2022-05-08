package study.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import study.cafe.entity.CafeKeyword
import study.cafe.repository.CafeKeywordRepository

@Transactional(readOnly = true)
@Service
class KeywordService(
    private val keywordRepository: CafeKeywordRepository
) {

    fun getKeywords(): List<CafeKeyword> {
        return keywordRepository.findAll()
    }
}
