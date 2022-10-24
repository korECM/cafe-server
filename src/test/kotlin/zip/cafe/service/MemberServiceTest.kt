package zip.cafe.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import zip.cafe.connector.S3Connector
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.ProfileImageRepository
import zip.cafe.seeds.createMember

class MemberServiceTest : FreeSpec({

    val memberRepository = mockk<MemberRepository>(relaxed = true)
    val profileImageRepository = mockk<ProfileImageRepository>(relaxed = true)
    val s3Connector = mockk<S3Connector>(relaxed = true)
    val reviewImageBucket = "test-bucket"
    val memberService = MemberService(
        memberRepository = memberRepository,
        profileImageRepository = profileImageRepository,
        s3Connector = s3Connector,
        reviewImageBucket = reviewImageBucket
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

    afterTest { clearMocks(memberRepository) }
})
