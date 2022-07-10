package zip.cafe.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import zip.cafe.repository.CafeRepository
import zip.cafe.repository.MemberFollowRepository
import zip.cafe.repository.ReviewRepository
import zip.cafe.seeds.createCafe

class CafeServiceTest : FreeSpec({

    val cafeRepository = mockk<CafeRepository>(relaxed = true)
    val reviewRepository = mockk<ReviewRepository>(relaxed = true)
    val memberFollowRepository = mockk<MemberFollowRepository>(relaxed = true)
    val cafeService = CafeService(cafeRepository = cafeRepository, reviewRepository = reviewRepository, memberFollowRepository = memberFollowRepository)

    "findById" - {
        "id로 카페를 가져온다" {
            // given
            val cafeId = 15L
            val cafe = createCafe(id = cafeId)
            // mock
            every { cafeRepository.findByIdOrNull(cafeId) } returns cafe
            // when
            val findCafe = cafeService.findById(cafeId)
            // then
            findCafe shouldBe cafe
        }
        "해당 id의 카페가 없으면 NoSuchElementException 예외를 던진다" {
            // given
            val cafeId = 15L
            // mock
            every { cafeRepository.findByIdOrNull(cafeId) } returns null
            // when
            shouldThrow<NoSuchElementException> { cafeService.findById(cafeId) }
        }
    }

    afterTest { clearMocks(cafeRepository) }
})
