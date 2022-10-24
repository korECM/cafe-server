package zip.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import zip.cafe.entity.ProfileImage

fun ProfileImageRepository.findOneById(id: Long) = this.findByIdOrNull(id) ?: throw NoSuchElementException()

interface ProfileImageRepository : JpaRepository<ProfileImage, Long> {
    fun findByIdIn(ids: List<Long>): List<ProfileImage>
}
