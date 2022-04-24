package study.cafe.service.dto

import study.cafe.entity.*
import study.cafe.entity.member.Member

data class ReviewRegisterDto(
    val cafe: Cafe,
    val member: Member,
    val visitPurpose: Purpose,
    val visitPurposeScore: IntScore,
    val foodInfos: List<FoodInfo>,
    val keywords: List<Long>,
    val description: String,
    val finalScore: FloatScore
) {

    data class FoodInfo(
        val food: Food,
        val score: IntScore
    )
}
