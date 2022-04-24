package study.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import study.cafe.entity.Review

interface ReviewRepository : JpaRepository<Review, Long>
