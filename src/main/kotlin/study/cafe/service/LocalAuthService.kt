package study.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import study.cafe.api.dto.local.LocalSignUpRequest
import study.cafe.repository.LocalAuthRepository
import study.cafe.repository.MemberRepository

@Transactional(readOnly = true)
@Service
class LocalAuthService(
    private val localAuthRepository: LocalAuthRepository,
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun signUp(dto: LocalSignUpRequest): Long {
        check(!memberRepository.existsByNicknameIs(dto.nickname)) {
            "이미 사용중인 닉네임입니다"
        }

        val member = dto.toMember()
        val localAuth = dto.toLocalAuth(member)

        memberRepository.save(member)
        localAuthRepository.save(localAuth)
        return member.id
    }
}