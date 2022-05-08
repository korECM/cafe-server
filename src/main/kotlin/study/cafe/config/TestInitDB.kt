package study.cafe.config

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import study.cafe.entity.Cafe
import study.cafe.entity.CafeKeyword
import study.cafe.entity.auth.LocalAuth
import study.cafe.entity.member.Gender.MALE
import study.cafe.entity.member.Member
import study.cafe.security.jwt.JwtTokenProvider
import study.cafe.util.logger
import java.time.LocalDate
import java.util.*
import javax.annotation.PostConstruct
import javax.persistence.EntityManager

@Profile("test")
@Component
class TestInitDB(
    private val initService: InitService,
) {

    @PostConstruct
    private fun init() {
        initService.dbInit()
    }

    @Component
    @Transactional
    class InitService(
        private val em: EntityManager,
        private val jwtTokenProvider: JwtTokenProvider
    ) {
        fun dbInit() {
            val member1 = createMember("길동길동", "기르동이")
            createLocalAuth(member1, "testId", "testPw")
            logger().info("Test User Token {}", jwtTokenProvider.createToken(member1.id, Date()))
            val member2 = createMember("기리기리", "리게")
            createLocalAuth(member2, "1234", "1234")
            val member3 = createMember("소소임", "커피")
            createLocalAuth(member3, "asdf", "asdf")
            createCafe()
            createKeywords()
        }

        private fun createMember(name: String, nickName: String): Member {
            val member = Member(
                name = name,
                nickname = nickName,
                birthDay = LocalDate.of(2000, 2, 20),
                gender = MALE
            )
            em.persist(member)
            return member
        }

        private fun createLocalAuth(member: Member, id: String, password: String) {
            val localAuth = LocalAuth(
                localId = id,
                localPassword = password,
                member = member
            )
            em.persist(localAuth)
        }

        private fun createCafe(): Cafe {
            val cafe = Cafe()
            em.persist(cafe)
            return cafe
        }

        private fun createKeywords() {
            val keywords = listOf(
                CafeKeyword(emoji = "\uD83D\uDECB", keyword = "아늑한"),
                CafeKeyword(emoji = "\uD83D\uDCD6", keyword = "조용한"),
                CafeKeyword(emoji = "\uD83C\uDF42", keyword = "감성적인"),
                CafeKeyword(emoji = "\uD83C\uDF81", keyword = "이색적인"),
                CafeKeyword(emoji = "\uD83D\uDC8E", keyword = "고급스러운"),
                CafeKeyword(emoji = "✨", keyword = "깔끔한"),
                CafeKeyword(emoji = "\uD83D\uDE04", keyword = "친절한"),
                CafeKeyword(emoji = "\uD83D\uDD09", keyword = "시끄러운"),
                CafeKeyword(emoji = "☕", keyword = "평범한"),
                CafeKeyword(emoji = "✈", keyword = "천장이 높은"),
                CafeKeyword(emoji = "\uD83D\uDD26", keyword = "어두운"),
                CafeKeyword(emoji = "\uD83C\uDF1F", keyword = "밝은"),
                CafeKeyword(emoji = "\uD83C\uDFDE", keyword = "뷰가 좋은"),
            )
            keywords.forEach(em::persist)
        }
    }
}
