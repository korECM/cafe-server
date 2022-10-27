package zip.cafe.service.profile

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.api.profile.dto.CheckProfileResult
import zip.cafe.api.profile.dto.ProfileFootprintInfo
import zip.cafe.api.profile.dto.ProfileFootprintInfo.Companion.from
import zip.cafe.api.profile.dto.ProfileInfo
import zip.cafe.api.profile.dto.ProfileReviewInfo
import zip.cafe.api.profile.dto.ProfileReviewInfo.Companion.from
import zip.cafe.repository.FootprintRepository
import zip.cafe.repository.MemberFollowRepository
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.ReviewRepository

@Service
class ProfileService(
    private val memberRepository: MemberRepository,
    private val memberFollowRepository: MemberFollowRepository,
    private val reviewRepository: ReviewRepository,
    private val footprintRepository: FootprintRepository,
) {
    @Transactional(readOnly = true)
    fun checkProfileInit(memberId: Long): CheckProfileResult {
        val member = requireNotNull(memberRepository.findByIdOrNull(memberId)) { "사용자를 찾을 수 없습니다" }
        return CheckProfileResult.from(member)
    }

    @Transactional(readOnly = true)
    fun getProfile(loginMemberId: Long, targetMemberId: Long): ProfileInfo {
        val member = requireNotNull(memberRepository.findByIdOrNull(targetMemberId)) { "사용자를 찾을 수 없습니다" }
        val isFollowing = memberFollowRepository.checkForFollowing(loginMemberId, targetMemberId)
        return ProfileInfo.from(member, isFollowing)
    }

    @Transactional(readOnly = true)
    fun getReview(memberId: Long): List<ProfileReviewInfo> {
        val reviews = reviewRepository.findAllNewestReviewByMemberId(memberId)
        return reviews.map(::from)
    }

    @Transactional(readOnly = true)
    fun getFootprint(memberId: Long): List<ProfileFootprintInfo> {
        val footprints = footprintRepository.findAllNewestFootprintByMemberId(memberId)
        return footprints.map(::from)
    }
}
