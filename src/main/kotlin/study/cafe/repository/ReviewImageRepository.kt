package study.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import study.cafe.entity.ReviewImage

interface ReviewImageRepository : JpaRepository<ReviewImage, Long>
