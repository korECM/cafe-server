package zip.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import zip.cafe.entity.review.Footprint

fun FootprintRepository.findOneById(id: Long) = this.findByIdOrNull(id) ?: throw NoSuchElementException()

interface FootprintRepository : JpaRepository<Footprint, Long>
