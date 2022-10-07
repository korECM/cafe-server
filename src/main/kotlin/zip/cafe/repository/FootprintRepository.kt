package zip.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository
import zip.cafe.entity.review.Footprint

interface FootprintRepository : JpaRepository<Footprint, Long>
