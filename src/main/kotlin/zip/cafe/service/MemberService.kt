package zip.cafe.service

import org.springframework.stereotype.Service
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.findOneById

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun checkNicknameDuplication(nickname: String) = memberRepository.existsByNicknameIs(nickname)

    fun findMemberById(memberId: Long) = memberRepository.findOneById(memberId)
}
