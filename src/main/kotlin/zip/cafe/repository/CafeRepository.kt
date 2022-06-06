package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import zip.cafe.entity.cafe.Cafe

fun CafeRepository.findOneById(id: Long) = this.findByIdOrNull(id) ?: throw NoSuchElementException()

interface CafeRepository : JpaRepository<Cafe, Long>, CafeRepositoryCustom
