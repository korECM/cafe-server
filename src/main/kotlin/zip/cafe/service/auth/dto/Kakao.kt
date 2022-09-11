package zip.cafe.service.auth.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class KakaoTokenInfo(
    val id: Long,
    @JsonProperty("expires_in")
    val expiresIn: Number,
    @JsonProperty("app_id")
    val appId: Number
)

data class KakaoUserInfo(
    val id: Long,
    @JsonProperty("connected_at")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Asia/Seoul")
    val connectedAt: LocalDateTime,
    val properties: KakaoUserProperty,
    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoUserAccount
) {
    val profile = kakaoAccount.profile
}

data class KakaoUserProperty(
    val nickname: String,
    @JsonProperty("profile_image")
    val profileImageURL: String,
    @JsonProperty("thumbnail_image")
    val thumbnailImageURL: String
)

data class KakaoUserAccount(
//    @JsonProperty("profile_nickname_needs_agreement")
//    val profileNicknameNeedsAgreement: Boolean,
//    @JsonProperty("profile_image_needs_agreement")
//    val profileImageNeedsAgreement: Boolean,
    val profile: KakaoUserProfile,
//    @JsonProperty("has_age_range")
//    val hasAgeRange: Boolean,
//    @JsonProperty("age_range_needs_agreement")
//    val ageRangeNeedsAgreement: Boolean,
//    @JsonProperty("age_range")
//    val ageRange: String,
//    @JsonProperty("has_birthday")
//    val hasBirthday: Boolean,
//    @JsonProperty("birthday_needs_agreement")
//    val birthdayNeedsAgreement: Boolean,
//    val birthday: String,
//    @JsonProperty("birthday_type")
//    val birthdayType: String,
//    @JsonProperty("has_gender")
//    val hasGender: Boolean,
//    @JsonProperty("gender_needs_agreement")
//    val genderNeedsAgreement: Boolean,
//    val gender: String
)

data class KakaoUserProfile(
    val nickname: String,
    @JsonProperty("profile_image_url")
    val profileImageURL: String,
    @JsonProperty("thumbnail_image_url")
    val thumbnailImageURL: String,
    @JsonProperty("is_default_image")
    val isDefaultImage: Boolean,
)
