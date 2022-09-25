package zip.cafe.service.profile

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.api.profile.dto.ProfileInfo
import zip.cafe.repository.MemberRepository

@Service
class ProfileService(
    private val memberRepository: MemberRepository
) {

    @Transactional(readOnly = true)
    fun getProfile(memberId: Long): ProfileInfo {
        val member = requireNotNull(memberRepository.findByIdOrNull(memberId)) { "사용자를 찾을 수 없습니다" }
        return ProfileInfo.from(member)
    }
}
