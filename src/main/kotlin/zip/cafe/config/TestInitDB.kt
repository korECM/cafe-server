package zip.cafe.config

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import zip.cafe.entity.Point
import zip.cafe.entity.ReviewImage
import zip.cafe.entity.auth.LocalAuth
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.member.Member
import zip.cafe.entity.menu.Menu
import zip.cafe.entity.review.CafeKeyword
import zip.cafe.entity.review.Footprint
import zip.cafe.entity.review.Review
import zip.cafe.entity.toScore
import zip.cafe.security.jwt.JwtTokenProvider
import zip.cafe.util.createPoint
import zip.cafe.util.logger
import java.time.LocalDate
import java.time.LocalDate.now
import java.util.*
import javax.annotation.PostConstruct
import javax.persistence.EntityManager

@Profile("test_data")
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
            val member1 = createMember("기르동이")
            createLocalAuth(member1, "testId", "testPw")
            logger().info("\n\nTest User Token \n\n{}\n\n", jwtTokenProvider.createToken(member1.id, nickname = member1.nickname, at = Date()))
            val member2 = createMember("리게")
            createLocalAuth(member2, "1234", "1234")
            val member3 = createMember("커피")
            createLocalAuth(member3, "asdf", "asdf")

            member1.follow(member2)
            member1.follow(member3)

            val keywords = createKeywords()

            val cafe1 = createCafe("북앤레스트", "서울 강남구 삼성로104길 22 1층", createPoint(127.05655307, 37.51095058), "오전 7:00–오후 10:00")
            val menu1Of1 = createMenu("아이스 아메리카노", 5000)
            val menu1Of2 = createMenu("아이스 카페 라떼", 5500)
            val menu1Of3 = createMenu("당근 케이크", 4000)
            cafe1.addMenu(menu1Of1)
            cafe1.addMenu(menu1Of2)
            cafe1.addMenu(menu1Of3)

            val cafe2 = createCafe("스타벅스 삼성현대힐점", "서울 강남구 삼성로 605", createPoint(127.05275451, 37.51352381), "10:00–20:00")
            val menu2Of1 = createMenu("아이스 아메리카노", 4000)
            val menu2Of2 = createMenu("플랫 화이트", 8000)
            cafe2.addMenu(menu2Of1)
            cafe2.addMenu(menu2Of2)

            val cafe3 = createCafe("오금동 커피", "서울 서초구 서초중앙로 238 가든리체프라자 2층 251호 (반포동)", createPoint(127.05235451, 37.51352381), "10:30–21:00")
            val menu3Of1 = createMenu("아이스 아메리카노", 3000)
            val menu3Of2 = createMenu("아이스 카페 라떼", 2000)
            val menu3Of3 = createMenu("아이스 바닐라 카페 라떼", 4800)
            val menu3Of4 = createMenu("아이스 콜드브루", 4900)
            val menu3Of5 = createMenu("아이스 콜브드루 라떼", 5400)
            cafe3.addMenu(menu3Of1)
            cafe3.addMenu(menu3Of2)
            cafe3.addMenu(menu3Of3)
            cafe3.addMenu(menu3Of4)
            cafe3.addMenu(menu3Of5)

            val reviewImage1Of1 = createReviewImage(member1, "https://media-cdn.tripadvisor.com/media/photo-s/1c/0d/58/75/interior.jpg")
            val reviewImage2Of1 = createReviewImage(member1, "https://images.homify.com/c_fill,f_auto,q_0,w_740/v1497622888/p/photo/image/2067284/JAY_0354.jpg")
            val footprint1 = createFootprint(member1, cafe1, now())
            val review1 = createReview(footprint1, 3.5, "설명 1")
            reviewImage1Of1.assignReview(review1)
            reviewImage2Of1.assignReview(review1)
            val reviewImage1Of2 = createReviewImage(member1, "https://media-cdn.tripadvisor.com/media/photo-s/19/15/a7/68/gazzi-cafe.jpg")
            val footprint2 = createFootprint(member1, cafe2, now().minusDays(3))
            val review2 = createReview(footprint2, 4.5, "설명인 것")
            reviewImage1Of2.assignReview(review2)
            val footprint3 = createFootprint(member2, cafe1, now().minusDays(5).minusMonths(1))
            val review3 = createReview(footprint3, 1.5, "또 다른 설명")
            val reviewImage1Of3 = createReviewImage(member2, "https://media-cdn.tripadvisor.com/media/photo-s/19/15/a7/68/gazzi-cafe.jpg")
            reviewImage1Of3.assignReview(review3)
            val footprint4 = createFootprint(member3, cafe1, now().minusDays(5).minusWeeks(1))
            val review4 = createReview(footprint4, 1.5, "카페 리뷰1")
            val footprint5 = createFootprint(member3, cafe2, now().plusDays(5).minusWeeks(2))
            val review5 = createReview(footprint5, 2.5, "카페 리뷰2")
            val footprint6 = createFootprint(member3, cafe3, now().plusDays(3).minusWeeks(1))
            val review6 = createReview(footprint6, 3.5, "카페 리뷰3")

            createFootprint(member2, cafe2, now())
            createFootprint(member1, cafe1, now().minusWeeks(13))
            createFootprint(member1, cafe3, now().plusDays(2))
            createFootprint(member3, cafe3, now().minusWeeks(2))

            review1.addCafeKeyword(keywords[0])
            review1.addCafeKeyword(keywords[1])
            review2.addCafeKeyword(keywords[3])
            review2.addCafeKeyword(keywords[4])
            review4.addCafeKeyword(keywords[5])
            review5.addCafeKeyword(keywords[6])
            review5.addCafeKeyword(keywords[7])
            review6.addCafeKeyword(keywords[0])
            review6.addCafeKeyword(keywords[1])
            review6.addCafeKeyword(keywords[2])

            review2.addLiker(member2)
            review2.addLiker(member3)
            review3.addLiker(member1)
        }

        private fun createMember(nickName: String, profileImageURL: String = Member.DEFAULT_PROFILE_IMAGE_URL): Member {
            val member = Member(
                nickname = nickName,
                profileImage = profileImageURL,
            )
            em.persist(member)
            return member
        }

        private fun createMenu(name: String, price: Long): Menu {
            val menu = Menu(
                name = name,
                price = price
            )
            em.persist(menu)
            return menu
        }

        private fun createLocalAuth(member: Member, id: String, password: String) {
            val localAuth = LocalAuth(
                localId = id,
                localPassword = password,
                member = member
            )
            em.persist(localAuth)
        }

        private fun createCafe(name: String, address: String, location: Point, openingHours: String): Cafe {
            val cafe = Cafe(name = name, address = address, location = location, openingHours = openingHours)
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

        private fun createFootprint(member: Member, cafe: Cafe, visitDate: LocalDate): Footprint =
            Footprint.from(member = member, cafe = cafe, visitDate = visitDate).apply {
                em.persist(this)
            }

        private fun createReview(footprint: Footprint, score: Double, description: String): Review =
            Review.from(
                footprint = footprint,
                finalScore = score.toScore(),
                description = description
            ).apply {
                em.persist(this)
            }
    }
}
