package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import zip.cafe.entity.review.Review

interface ReviewRepository : JpaRepository<Review, Long>
