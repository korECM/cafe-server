package zip.cafe.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.findOneById
import zip.cafe.seeds.createMember

class MemberFollowServiceTest : FreeSpec({

    val memberRepository = mockk<MemberRepository>(relaxed = true)
    val memberFollowService = MemberFollowService(
        memberRepository = memberRepository
    )

    "팔로우" - {
        "from이 to를 팔로우한 적이 없다면" - {
            "from이 to를 팔로우한다" {
                // given
                val fromMemberId = 3L
                val toMemberId = 5L
                val fromMember = createMember(id = fromMemberId)
                val toMember = createMember(id = toMemberId)
                // mock
                every { memberRepository.findOneById(fromMemberId) } returns fromMember
                every { memberRepository.findOneById(toMemberId) } returns toMember
                // when
                memberFollowService.follow(fromMemberId, toMemberId)
                // then
                fromMember.followees shouldContainExactly listOf(toMember)
                toMember.followers shouldContainExactly listOf(fromMember)
            }
        }
        "이미 from이 to를 팔로우한 상태라면" - {
            "그대로 from이 to를 다시 팔로우한다" {
                // given
                val fromMemberId = 3L
                val toMemberId = 5L
                val fromMember = createMember(id = fromMemberId)
                val toMember = createMember(id = toMemberId)
                // mock
                every { memberRepository.findOneById(fromMemberId) } returns fromMember
                every { memberRepository.findOneById(toMemberId) } returns toMember
                // when
                memberFollowService.follow(fromMemberId, toMemberId)
                memberFollowService.follow(fromMemberId, toMemberId)
                // then
                fromMember.followees shouldContainExactly listOf(toMember)
                toMember.followers shouldContainExactly listOf(fromMember)
            }
        }
    }

    afterTest {
        clearAllMocks()
    }
})
