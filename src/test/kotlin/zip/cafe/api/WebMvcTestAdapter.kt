package zip.cafe.api

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.mockk.clearMocks
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import zip.cafe.api.auth.AppleAuthController
import zip.cafe.api.auth.KakaoAuthController
import zip.cafe.api.controller.CommonResponseController
import zip.cafe.api.profile.ProfileController
import zip.cafe.api.recommandation.FriendController
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.service.*
import zip.cafe.service.auth.AppleAuthService
import zip.cafe.service.auth.AuthService
import zip.cafe.service.auth.KakaoAuthService
import zip.cafe.service.profile.ProfileService
import zip.cafe.service.recommendation.RecommendationService

@WebMvcTest(
    controllers = [
        CafeController::class,
        FeedController::class,
        CommonResponseController::class,
        FootPrintController::class,
        KeywordController::class,
        MemberController::class,
        MemberFollowController::class,
        ReviewController::class,
        SearchController::class,
        ProfileController::class,
        AppleAuthController::class,
        KakaoAuthController::class,
        FriendController::class,
    ]
)
open class WebMvcTestAdapter(body: FreeSpec.() -> Unit = {}) : WebMvcTestSpec(body) {

    @MockkBean
    protected lateinit var cafeService: CafeService

    @MockkBean
    protected lateinit var feedService: FeedService

    @MockkBean
    protected lateinit var reviewService: ReviewService

    @MockkBean
    protected lateinit var keywordService: KeywordService

    @MockkBean
    protected lateinit var memberService: MemberService

    @MockkBean
    protected lateinit var memberFollowService: MemberFollowService

    @MockkBean
    protected lateinit var reviewLikeService: ReviewLikeService

    @MockkBean
    protected lateinit var searchService: SearchService

    @MockkBean
    protected lateinit var profileService: ProfileService

    @MockkBean
    protected lateinit var appleAuthService: AppleAuthService

    @MockkBean
    protected lateinit var authService: AuthService

    @MockkBean
    protected lateinit var recommendationService: RecommendationService

    @MockkBean
    protected lateinit var kakaoAuthService: KakaoAuthService

    override suspend fun afterTest(testCase: TestCase, result: TestResult) {
        super.afterTest(testCase, result)
        clearMocks(
            cafeService,
            feedService,
            reviewService,
            keywordService,
            memberService,
            memberFollowService,
            reviewLikeService,
            searchService,
            profileService,
            appleAuthService,
            authService,
            kakaoAuthService,
            recommendationService
        )
    }
}
