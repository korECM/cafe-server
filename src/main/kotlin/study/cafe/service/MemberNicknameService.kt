package study.cafe.service

import org.springframework.stereotype.Service
import study.cafe.repository.MemberRepository

@Service
class MemberNicknameService(
    private val memberRepository: MemberRepository
) {
    fun checkNicknameDuplication(nickname: String): Boolean {
        return memberRepository.existsByNicknameIs(nickname)
    }
}
