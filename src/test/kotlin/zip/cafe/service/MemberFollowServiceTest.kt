package zip.cafe.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.clearMocks
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
                val fromMember = createMember(id = 3L)
                val toMember = createMember(id = 5L)
                // mock
                every { memberRepository.findOneById(fromMember.id) } returns fromMember
                every { memberRepository.findOneById(toMember.id) } returns toMember
                // when
                memberFollowService.follow(fromMember.id, toMember.id)
                // then
                fromMember.followees shouldContainExactly listOf(toMember)
                fromMember.followers shouldHaveSize 0
                toMember.followees shouldHaveSize 0
                toMember.followers shouldContainExactly listOf(fromMember)
            }
        }
        "이미 from이 to를 팔로우한 상태라면" - {
            "그대로 from이 to를 다시 팔로우한다" {
                // given
                val fromMember = createMember(id = 3L)
                val toMember = createMember(id = 5L)
                // mock
                every { memberRepository.findOneById(fromMember.id) } returns fromMember
                every { memberRepository.findOneById(toMember.id) } returns toMember
                // when
                memberFollowService.follow(fromMember.id, toMember.id)
                memberFollowService.follow(fromMember.id, toMember.id)
                // then
                fromMember.followees shouldContainExactly listOf(toMember)
                fromMember.followers shouldHaveSize 0
                toMember.followees shouldHaveSize 0
                toMember.followers shouldContainExactly listOf(fromMember)
            }
        }

        "언팔로우" - {
            "from이 to를 팔로우한 적이 있었다면" - {
                "성공적으로 언팔로우한다" {
                    // given
                    val fromMember = createMember(id = 3L)
                    val toMember = createMember(id = 5L)
                    val anotherMember = createMember(id = 7L)
                    // mock
                    every { memberRepository.findOneById(fromMember.id) } returns fromMember
                    every { memberRepository.findOneById(toMember.id) } returns toMember
                    every { memberRepository.findOneById(anotherMember.id) } returns anotherMember
                    // when
                    memberFollowService.follow(fromMember.id, toMember.id)
                    memberFollowService.follow(fromMember.id, anotherMember.id)
                    memberFollowService.unfollow(fromMember.id, toMember.id)
                    // then
                    fromMember.followees shouldContainExactly listOf(anotherMember)
                    toMember.followers shouldHaveSize 0
                    anotherMember.followers shouldContainExactly listOf(fromMember)
                }
            }
            "from이 to를 팔로우한 적이 없었다면" - {
                "기존 팔로우, 팔로워 목록은 유지되고 아무런 일도 일어나지 않는다" {
                    // given
                    val fromMember = createMember(id = 3L)
                    val toMember = createMember(id = 5L)
                    val anotherMember = createMember(id = 7L)
                    // mock
                    every { memberRepository.findOneById(fromMember.id) } returns fromMember
                    every { memberRepository.findOneById(toMember.id) } returns toMember
                    every { memberRepository.findOneById(anotherMember.id) } returns anotherMember
                    // when
                    memberFollowService.follow(fromMember.id, toMember.id)
                    memberFollowService.unfollow(fromMember.id, anotherMember.id)
                    // then
                    fromMember.followees shouldContainExactly listOf(toMember)
                    toMember.followers shouldContainExactly listOf(fromMember)
                    anotherMember.followers shouldHaveSize 0
                }
            }
        }
    }

    afterTest { clearMocks(memberRepository) }
})
