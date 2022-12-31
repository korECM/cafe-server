package zip.cafe.config

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import zip.cafe.entity.*
import zip.cafe.entity.auth.LocalAuth
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.member.Member
import zip.cafe.entity.menu.Menu
import zip.cafe.entity.review.CafeKeyword
import zip.cafe.entity.review.Footprint
import zip.cafe.entity.review.Purpose
import zip.cafe.entity.review.Review
import zip.cafe.security.jwt.JwtTokenProvider
import zip.cafe.service.MemberFollowService
import zip.cafe.service.ReviewLikeService
import zip.cafe.service.ReviewService
import zip.cafe.service.dto.ReviewRegisterDto
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
        private val jwtTokenProvider: JwtTokenProvider,
        private val reviewService: ReviewService,
        private val reviewLikeService: ReviewLikeService,
        private val memberFollowService: MemberFollowService
    ) {
        fun dbInit() {
            val member1 = createMember("기르동이")
            createLocalAuth(member1, "testId", "testPw")
            logger().info("\n\nTest User Token \n\n{}\n\n", jwtTokenProvider.createToken(member1.id, nickname = member1.nickname, at = Date()))
            val member2 = createMember("리게")
            createLocalAuth(member2, "1234", "1234")
            val member3 = createMember("커피")
            createLocalAuth(member3, "asdf", "asdf")

            memberFollowService.follow(member1.id, member2.id)
            memberFollowService.follow(member1.id, member3.id)

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
            val footprint1Id = reviewService.createFootprint(cafe1.id, member1.id, now())

            val review1Id = reviewService.createReview(
                footprint1Id,
                member1.id,
                ReviewRegisterDto(
                    visitPurpose = Purpose.DATE,
                    visitPurposeScore = 5.toScore(),
                    foodInfos = listOf(ReviewRegisterDto.FoodInfo(Food.BAKERY, 3.toScore()), ReviewRegisterDto.FoodInfo(Food.COFFEE, 5.toScore())),
                    keywords = listOf(keywords[0].id, keywords[1].id),
                    reviewImageIds = listOf(reviewImage1Of1.id, reviewImage2Of1.id),
                    description = "조용하고 좋아요!",
                    finalScore = 3.0.toScore(),
                )
            )

            val reviewImage1Of2 = createReviewImage(member1, "https://media-cdn.tripadvisor.com/media/photo-s/19/15/a7/68/gazzi-cafe.jpg")
            val footprint2Id = reviewService.createFootprint(cafe2.id, member1.id, now().minusDays(3))
            val review2Id = reviewService.createReview(
                footprint2Id,
                memberId = member1.id,
                dto = ReviewRegisterDto(
                    visitPurpose = Purpose.STUDY,
                    visitPurposeScore = 3.toScore(),
                    foodInfos = listOf(),
                    keywords = listOf(keywords[3].id, keywords[4].id),
                    reviewImageIds = listOf(reviewImage1Of2.id),
                    description = "사장님이 친절하셔서 애정하는 카페",
                    finalScore = 4.0.toScore()
                ),
            )

            val reviewImage1Of3 = createReviewImage(member2, "https://media-cdn.tripadvisor.com/media/photo-s/19/15/a7/68/gazzi-cafe.jpg")
            val footprint3Id = reviewService.createFootprint(cafe1.id, member2.id, now().minusDays(5).minusMonths(1))
            val review3Id = reviewService.createReview(
                footprint3Id,
                memberId = member2.id,
                dto = ReviewRegisterDto(
                    visitPurpose = Purpose.ETC,
                    visitPurposeScore = 2.toScore(),
                    foodInfos = listOf(),
                    keywords = listOf(keywords[0].id, keywords[3].id, keywords[4].id),
                    reviewImageIds = listOf(reviewImage1Of3.id),
                    description = "그럭저럭인 카페",
                    finalScore = 1.0.toScore()
                ),
            )

            val footprint4Id = reviewService.createFootprint(cafe1.id, member3.id, now().minusDays(5).minusWeeks(1))
            val review4Id = reviewService.createReview(
                footprint4Id,
                memberId = member3.id,
                dto = ReviewRegisterDto(
                    visitPurpose = Purpose.TALK,
                    visitPurposeScore = 1.toScore(),
                    foodInfos = listOf(),
                    keywords = listOf(keywords[0].id, keywords[1].id, keywords[3].id, keywords[5].id),
                    reviewImageIds = listOf(),
                    description = "너무 시끄러워요",
                    finalScore = 1.0.toScore()
                ),
            )

            val footprint5Id = reviewService.createFootprint(cafe2.id, member3.id, now().plusDays(5).minusWeeks(2))
            val review5Id = reviewService.createReview(
                footprint5Id,
                memberId = member3.id,
                dto = ReviewRegisterDto(
                    visitPurpose = Purpose.STUDY,
                    visitPurposeScore = 5.toScore(),
                    foodInfos = listOf(),
                    keywords = listOf(keywords[6].id, keywords[7].id),
                    reviewImageIds = listOf(),
                    description = "조용해서 공부하긴 좋아요 근데 커피는 노맛",
                    finalScore = 2.0.toScore()
                ),
            )

            val footprint6Id = reviewService.createFootprint(cafe3.id, member3.id, now().plusDays(3).minusWeeks(1))
            val review6Id = reviewService.createReview(
                footprint6Id,
                memberId = member3.id,
                dto = ReviewRegisterDto(
                    visitPurpose = Purpose.DATE,
                    visitPurposeScore = 3.toScore(),
                    foodInfos = listOf(),
                    keywords = listOf(keywords[0].id, keywords[1].id, keywords[2].id),
                    reviewImageIds = listOf(),
                    description = "인테리어 나쁘지 않아요",
                    finalScore = 3.0.toScore()
                ),
            )

            reviewService.createFootprint(cafe2.id, member2.id, now())
            reviewService.createFootprint(cafe1.id, member1.id, now().minusWeeks(13))
            reviewService.createFootprint(cafe3.id, member1.id, now().minusDays(2))
            reviewService.createFootprint(cafe3.id, member3.id, now().minusWeeks(2).minusDays(3))

            reviewLikeService.likeReview(member2.id, review2Id)
            reviewLikeService.likeReview(member3.id, review2Id)
            reviewLikeService.likeReview(member1.id, review3Id)
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

        private fun createReview(footprint: Footprint, score: Double, visitPurpose: Purpose, visitPurposeScore: IntScore, description: String): Review =
            Review.from(
                footprint = footprint,
                finalScore = score.toScore(),
                visitPurpose = visitPurpose,
                visitPurposeScore = visitPurposeScore,
                description = description
            ).apply {
                em.persist(this)
            }
    }
}
