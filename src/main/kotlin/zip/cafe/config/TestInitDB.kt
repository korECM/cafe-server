package zip.cafe.config

import org.locationtech.jts.geom.Point
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import zip.cafe.entity.ReviewImage
import zip.cafe.entity.auth.LocalAuth
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.member.Gender.MALE
import zip.cafe.entity.member.Member
import zip.cafe.entity.review.CafeKeyword
import zip.cafe.entity.review.Review
import zip.cafe.entity.toScore
import zip.cafe.security.jwt.JwtTokenProvider
import zip.cafe.util.createPoint
import zip.cafe.util.logger
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
            logger().info("\n\nTest User Token \n\n{}\n\n", jwtTokenProvider.createToken(member1.id, Date()))
            val member2 = createMember("기리기리", "리게")
            createLocalAuth(member2, "1234", "1234")
            val member3 = createMember("소소임", "커피")
            createLocalAuth(member3, "asdf", "asdf")

            member1.follow(member2)
            member1.follow(member3)

            val keywords = createKeywords()

            val cafe1 = createCafe("북앤레스트", "서울 강남구 삼성로104길 22 1층", createPoint(127.05655307, 37.51095058))
            val cafe2 = createCafe("스타벅스 삼성현대힐점", "서울 강남구 삼성로 605", createPoint(127.05275451, 37.51352381))

            val reviewImage1Of1 = createReviewImage(member1, "https://media-cdn.tripadvisor.com/media/photo-s/1c/0d/58/75/interior.jpg")
            val reviewImage2Of1 = createReviewImage(member1, "https://images.homify.com/c_fill,f_auto,q_0,w_740/v1497622888/p/photo/image/2067284/JAY_0354.jpg")
            val review1 = createReview(member1, cafe1, 3.5, "설명 1")
            reviewImage1Of1.assignReview(review1)
            reviewImage2Of1.assignReview(review1)
            val reviewImage1Of2 = createReviewImage(member1, "https://media-cdn.tripadvisor.com/media/photo-s/19/15/a7/68/gazzi-cafe.jpg")
            val review2 = createReview(member1, cafe2, 4.5, "설명인 것")
            reviewImage1Of2.assignReview(review2)
            val review3 = createReview(member2, cafe1, 1.5, "또 다른 설명")
            val review4 = createReview(member3, cafe2, 2.5, "카페 리뷰")

            review1.addCafeKeyword(keywords[0])
            review1.addCafeKeyword(keywords[1])
            review2.addCafeKeyword(keywords[3])
            review2.addCafeKeyword(keywords[4])
            review4.addCafeKeyword(keywords[5])

            review2.addLiker(member2)
            review2.addLiker(member3)
            review3.addLiker(member1)
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

        private fun createCafe(name: String, address: String, location: Point): Cafe {
            val cafe = Cafe(name = name, address = address, location = location)
            em.persist(cafe)
            return cafe
        }

        private fun createKeywords(): List<CafeKeyword> {
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
            return keywords
        }

        private fun createReviewImage(uploadedBy: Member, url: String): ReviewImage {
            val reviewImage = ReviewImage.createWithoutReview(
                "test-bucket",
                "someName",
                url,
                url,
                uploadedBy
            )
            em.persist(reviewImage)
            return reviewImage
        }

        private fun createReview(member: Member, cafe: Cafe, score: Double, description: String): Review {
            val review = Review(
                cafe = cafe,
                member = member,
                finalScore = score.toScore(),
                description = description
            )
            em.persist(review)
            return review
        }
    }
}
