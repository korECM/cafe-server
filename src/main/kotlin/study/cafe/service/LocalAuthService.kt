package study.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import study.cafe.repository.LocalAuthRepository
import study.cafe.repository.MemberRepository
import study.cafe.repository.findByAuthId
import study.cafe.service.dto.LocalSignInDto
import study.cafe.service.dto.LocalSignUpDto

@Transactional(readOnly = true)
@Service
class LocalAuthService(
    private val localAuthRepository: LocalAuthRepository,
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun signUp(dto: LocalSignUpDto): Long {
        check(!memberRepository.existsByNicknameIs(dto.nickname)) {
            "이미 사용중인 닉네임입니다"
        }

        val member = dto.toMember()
        val localAuth = dto.toLocalAuth(member)

        memberRepository.save(member)
        localAuthRepository.save(localAuth)
        return member.id
    }

    @Transactional
    fun signIn(dto: LocalSignInDto): Long {
        val auth = localAuthRepository.findByAuthId(dto.id)
        requireNotNull(auth) { "id가 존재하지 않거나 비밀번호가 일치하지 않습니다" }

        return auth.member.id
    }
}
