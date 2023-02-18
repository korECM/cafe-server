package zip.cafe.api.dto

import zip.cafe.entity.Food
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.member.Member
import zip.cafe.entity.review.Purpose
import zip.cafe.entity.review.ReviewCafeKeyword

data class MemberSearchRequest(
    val name: String,
)

data class CafeSearchRequest(
    val name: String,
    val visitPurposeList: List<Purpose>,
    val foodList: List<Food>,
    val keywordIdList: List<Long>,
    val leftTopLatitude: Double,
    val leftTopLongitude: Double,
    val rightBottomLatitude: Double,
    val rightBottomLongitude: Double,
    val minCafeId: Long? = null,
    val limit: Long = 30,
)

data class KeywordSearchRequest(
    val name: String,
)

data class CafeInfo(
    val id: Long,
    val name: String,
    val image: String,
    val address: String,
    val numberOfReviews: Long,
    val numberOfFootPrints: Long,
    val averageScore: Double,
    val position: Position,
) {
    companion object {
        fun from(cafe: Cafe): CafeInfo {
            return CafeInfo(
                id = cafe.id,
                name = cafe.name,
                // TODO 카페 사진 어떻게 고르지
                image = "https://images.chosun.com/resizer/08-lUWhtJ5pJpORejo8xPXHKyBE=/600x655/smart/cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/57OEJVMO3RCD3K5LXOWT456IPY.jpg",
                address = cafe.address,
                numberOfReviews = cafe.reviewCount,
                numberOfFootPrints = cafe.footPrintCount,
                averageScore = cafe.totalScore / cafe.reviewCount,
                position = Position(
                    latitude = cafe.location.latitude,
                    longitude = cafe.location.longitude,
                )
            )
        }
    }
}

data class Position(
    val latitude: Double,
    val longitude: Double,
)

data class KeywordInfo(
    val id: Long,
    val keyword: String,
    val numberOfReviews: Int,
) {
    companion object {
        fun from(reviewCafeKeyword: List<ReviewCafeKeyword>): List<KeywordInfo> {
            return reviewCafeKeyword.groupBy { it.cafeKeyword }
                .map { KeywordInfo(it.key.id, it.key.keyword, it.value.size) }
        }
    }
}

data class MemberInfo(
    val id: Long,
    val name: String,
    val image: String,
    val description: String
) {

    companion object {
        fun from(member: Member): MemberInfo {
            return MemberInfo(
                member.id,
                member.nickname,
                member.profileImage,
                "어쩌구팬카페"
            )
        }
    }
}
