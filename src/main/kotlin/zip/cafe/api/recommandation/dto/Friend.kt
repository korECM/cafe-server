package zip.cafe.api.recommandation.dto

data class FriendRecommendationReviewImage(
    val id: Long,
    val imageURL: String,
)

data class FriendRecommendationResult(
    val memberId: Long,
    val profileImageURL : String,
    val numberOfReviews: Long,
    val numberOfFollower: Long,
    val reviewImages: List<FriendRecommendationReviewImage>,
)

data class FriendRecommendationResponse(
    val friends: List<FriendRecommendationResult>
)