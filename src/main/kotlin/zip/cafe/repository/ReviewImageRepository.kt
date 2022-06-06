package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import zip.cafe.entity.ReviewImage

interface ReviewImageRepository : JpaRepository<ReviewImage, Long> {
    fun findByIdIn(ids: List<Long>): List<ReviewImage>
}
