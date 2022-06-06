package zip.cafe.entity.member

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.*
import zip.cafe.seeds.createMember

class MemberFollowTest : FreeSpec({
    "팔로우" - {
        "a가 b를 팔로우하면 b는 a의 팔로우 목록에 들어가고 a는 b의 팔로워 목록에 들어간다" {
            // given
            val a = createMember()
            val b = createMember()
            // when
            a.follow(b)
            // then
            a.followees shouldContain b
            b.followers shouldContain a
        }

        "a가 이미 b를 팔로우했는데 또 팔로우를 하더라도 a의 팔로우 목록에는 b 하나만 있고 b의 팔로워 목록에도 a 하나만 있다" {
            // given
            val a = createMember()
            val b = createMember()
            // when
            a.follow(b)
            a.follow(b)
            // then
            a.followees shouldContainExactly listOf(b)
            b.followers shouldContainExactly listOf(a)
        }

        "a가 b와 c를 팔로우하고 b가 a를 팔로우하면 a의 팔로우 목록에는 b와 c, b의 팔로우 목록에는 c, a의 팔로워 목록에는 b, c의 팔로워 목록에는 a가 있다" {
            // given
            val a = createMember()
            val b = createMember()
            val c = createMember()
            // when
            a.follow(b)
            println(a.name)
            println(b.name)
            println(c.name)
            a.follow(c)
            b.follow(a)
            // then
            a.followees shouldContainAll listOf(b, c)
            b.followees shouldContainAll listOf(a)
            c.followees shouldHaveSize 0

            a.followers shouldContainAll listOf(b)
            b.followers shouldContainAll listOf(a)
            c.followers shouldContainAll listOf(a)
        }
    }

    "언팔로우" - {
        "a가 b를 팔로우하고 언팔로우하면 b는 a의 팔로우 목록에 없으며 a는 b의 팔로워 목록에 없다" {
            // given
            val a = createMember()
            val b = createMember()
            a.follow(b)
            // when
            a.unfolow(b)
            // then
            a.followees shouldNotContain b
            b.followers shouldNotContain a
        }

        "팔로우한 적이 없는 멤버를 언팔로우하면 기존 팔로우, 또는 팔로워 목록이 변하지 않는다" {
            // given
            val a = createMember(name = "김")
            val b = createMember(name = "이")
            val c = createMember(name = "박")
            a.follow(b)

            val orgAFollowers = a.followers
            val orgAFollowees = a.followees
            val orgBFollowers = b.followers
            val orgBFollowees = b.followees
            val orgCFollowers = c.followers
            val orgCFollowees = c.followees
            // when
            a.unfolow(c)
            // then
            a.followers shouldContainExactly orgAFollowers
            a.followees shouldContainExactly orgAFollowees
            b.followers shouldContainExactly orgBFollowers
            b.followees shouldContainExactly orgBFollowees
            c.followers shouldContainExactly orgCFollowers
            c.followees shouldContainExactly orgCFollowees
        }
    }
})
