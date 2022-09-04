package zip.cafe.service.auth

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.repository.LocalAuthRepository
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.findByAuthId
import zip.cafe.security.PasswordEncoder
import zip.cafe.service.auth.dto.LocalSignInDto
import zip.cafe.service.auth.dto.LocalSignUpDto

private const val LOGIN_FAIL_MESSAGE = "id가 존재하지 않거나 비밀번호가 일치하지 않습니다"

@Transactional(readOnly = true)
@Service
class LocalAuthService(
    private val passwordEncoder: PasswordEncoder,
    private val localAuthRepository: LocalAuthRepository,
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun signUp(dto: LocalSignUpDto): Long {
        check(!memberRepository.existsByNicknameIs(dto.nickname)) {
            "이미 사용중인 닉네임입니다"
        }

        check(!localAuthRepository.existsByLocalIdIs(dto.id)) {
            "이미 사용중인 아이디입니다"
        }

        val member = dto.toMember()
        val localAuth = dto.toLocalAuth(member, passwordEncoder::encode)

        memberRepository.save(member)
        localAuthRepository.save(localAuth)
        return member.id
    }

    @Transactional
    fun signIn(dto: LocalSignInDto): Long {
        val auth = localAuthRepository.findByAuthId(dto.id)
        requireNotNull(auth) { LOGIN_FAIL_MESSAGE }

        check(passwordEncoder.matches(dto.password, auth.localPassword)) { LOGIN_FAIL_MESSAGE }

        return auth.member.id
    }
}
