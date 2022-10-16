package zip.cafe.service.profile

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.api.profile.dto.ProfileFootprintInfo
import zip.cafe.api.profile.dto.ProfileFootprintInfo.Companion.from
import zip.cafe.api.profile.dto.ProfileInfo
import zip.cafe.api.profile.dto.ProfileReviewInfo
import zip.cafe.api.profile.dto.ProfileReviewInfo.Companion.from
import zip.cafe.repository.FootprintRepository
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.ReviewRepository

@Service
class ProfileService(
    private val memberRepository: MemberRepository,
    private val reviewRepository: ReviewRepository,
    private val footprintRepository: FootprintRepository,
) {
    @Transactional(readOnly = true)
    fun getProfile(memberId: Long): ProfileInfo {
        val member = requireNotNull(memberRepository.findByIdOrNull(memberId)) { "사용자를 찾을 수 없습니다" }
        return ProfileInfo.from(member)
    }

    @Transactional(readOnly = true)
    fun getReview(memberId: Long): List<ProfileReviewInfo> {
        val reviews = reviewRepository.findAllByMemberId(memberId)
        return reviews.map(::from)
    }

    @Transactional(readOnly = true)
    fun getFootprint(memberId: Long): List<ProfileFootprintInfo> {
        val footprints = footprintRepository.findAllByMemberId(memberId)
        return footprints.map(::from)
    }
}
