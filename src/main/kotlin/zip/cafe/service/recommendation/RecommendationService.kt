package zip.cafe.service.recommendation

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.api.recommandation.dto.FriendRecommendationResult
import zip.cafe.api.recommandation.dto.FriendRecommendationReviewImage
import zip.cafe.repository.MemberFollowRepository
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.ReviewImageRepository

@Service
class RecommendationService(
    private val memberFollowRepository: MemberFollowRepository,
    private val memberRepository: MemberRepository,
    private val reviewImageRepository: ReviewImageRepository,
) {
    @Transactional(readOnly = true)
    fun getFriendRecommendation(loginMemberId: Long): List<FriendRecommendationResult> {
        val followeeIds = memberFollowRepository.getFolloweeIds(loginMemberId)
        val recommendMemberIds = reviewImageRepository.findReviewImageUploaderIdsNumberOfReviewImagesIsGreaterThanExceptMembers(followeeIds, 5)
        val recommendMembers = memberRepository.findAllById(recommendMemberIds)
        val recommendReviewImages = reviewImageRepository.findByUploadedByIn(recommendMembers).groupBy { it.uploadedBy.id }

        return recommendMembers.map {
            FriendRecommendationResult(
                memberId = it.id,
                profileImageURL = it.profileImage,
                numberOfReviews = it.reviewCount,
                numberOfFollower = it.followerCount,
                reviewImages = recommendReviewImages[it.id]?.map { reviewImage ->
                    FriendRecommendationReviewImage(reviewImage.id, reviewImage.cloudFrontURL)
                } ?: emptyList()
            )
        }
    }
}
