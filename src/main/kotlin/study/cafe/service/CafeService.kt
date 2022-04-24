package study.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import study.cafe.repository.CafeRepository
import study.cafe.repository.findOneById

@Transactional(readOnly = true)
@Service
class CafeService(
    private val cafeRepository: CafeRepository
) {

    fun findById(id: Long) = cafeRepository.findOneById(id)
}
