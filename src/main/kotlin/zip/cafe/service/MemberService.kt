package zip.cafe.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import zip.cafe.entity.member.Member
import zip.cafe.repository.MemberRepository

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun checkNicknameDuplication(nickname: String): Boolean {
        return memberRepository.existsByNicknameIs(nickname)
    }

    fun findMemberById(userId: Long): Member {
        return memberRepository.findByIdOrNull(userId) ?: throw IllegalArgumentException("존재하지 않는 유저입니다")
    }
}
