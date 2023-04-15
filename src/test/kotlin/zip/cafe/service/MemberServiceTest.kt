package zip.cafe.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import zip.cafe.connector.S3Connector
import zip.cafe.repository.*
import zip.cafe.seeds.createMember

class MemberServiceTest : FreeSpec({

    val memberRepository = mockk<MemberRepository>(relaxed = true)
    val profileImageRepository = mockk<ProfileImageRepository>(relaxed = true)
    val localAuthRepository = mockk<LocalAuthRepository>(relaxed = true)
    val kakaoAuthRepository = mockk<KakaoAuthRepository>(relaxed = true)
    val appleAuthRepository = mockk<AppleAuthRepository>(relaxed = true)
    val s3Connector = mockk<S3Connector>(relaxed = true)
    val reviewImageBucket = "test-bucket"
    val memberService = MemberService(
        memberRepository = memberRepository,
        profileImageRepository = profileImageRepository,
        s3Connector = s3Connector,
        reviewImageBucket = reviewImageBucket,
        localAuthRepository = localAuthRepository,
        kakaoAuthRepository = kakaoAuthRepository,
        appleAuthRepository = appleAuthRepository,
    )

    "findMemberById" - {
        "주어진 id로 멤버를 찾아서 반환한다" {
            // given
            val memberId = 35L
            val member = createMember(id = memberId)
            // mock
            every { memberRepository.findByIdOrNull(memberId) } returns member
            // when
            val findMember = memberService.findMemberById(memberId)
            // then
            findMember shouldBe member
        }
        "주어진 id의 멤버가 없다면 NoSuchElement 예외를 던진다" {
            // given
            val memberId = 2L
            // mock
            every { memberRepository.findByIdOrNull(memberId) } returns null
            // when
            shouldThrow<NoSuchElementException> { memberService.findMemberById(memberId) }
            // then
        }
    }

    "initMemberProfile" - {
        "이미 프로필을 초기화 한 경우 예외를 던진다" {
            // given
            val memberId = 5L
            val nickname = "nickname"
            val description = "description"
            val profileImageId = null
            // mock
            every { memberRepository.findByIdOrNull(memberId) } returns createMember(
                id = memberId,
                isProfileInit = true
            )
            // when
            shouldThrow<IllegalStateException> {
                memberService.initMemberProfile(
                    memberId,
                    nickname,
                    description,
                    profileImageId
                )
            }
            // then
        }
    }

    afterTest {
        clearMocks(memberRepository)
        clearMocks(profileImageRepository)
        clearMocks(localAuthRepository)
        clearMocks(kakaoAuthRepository)
        clearMocks(appleAuthRepository)
    }
})
