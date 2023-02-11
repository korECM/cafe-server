package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import zip.cafe.entity.ReviewImage
import zip.cafe.entity.member.Member

interface ReviewImageRepository : JpaRepository<ReviewImage, Long>, ReviewImageRepositoryCustom {
    fun findByIdIn(ids: List<Long>): List<ReviewImage>
    fun findByUploadedByIn(uploadedBy: List<Member>): List<ReviewImage>
}
