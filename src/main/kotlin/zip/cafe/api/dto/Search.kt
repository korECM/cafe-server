package zip.cafe.api.dto

import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.member.Member

data class CafeInfo(
    val id: Long,
    val name: String,
    val image: String,
    val address: String,
) {
    companion object {
        fun from(cafe: Cafe): CafeInfo {
            return CafeInfo(
                cafe.id,
                cafe.name,
                // TODO 카페 사진 어떻게 고르지
                "https://images.chosun.com/resizer/08-lUWhtJ5pJpORejo8xPXHKyBE=/600x655/smart/cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/57OEJVMO3RCD3K5LXOWT456IPY.jpg",
                cafe.address
            )
        }
    }
}

data class KeywordInfo(
    val id: Long,
    val name: String,
    val numberOfPosts: Long,
)


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
                "https://images.chosun.com/resizer/08-lUWhtJ5pJpORejo8xPXHKyBE=/600x655/smart/cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/57OEJVMO3RCD3K5LXOWT456IPY.jpg",
                "어쩌구팬카페"
            )
        }
    }
}
