package zip.cafe.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import zip.cafe.entity.Food
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.cafe.QCafe.cafe
import zip.cafe.entity.cafe.QCafeFoodStat.cafeFoodStat
import zip.cafe.entity.cafe.QCafeKeywordStat.cafeKeywordStat
import zip.cafe.entity.cafe.QCafePurposeStat.cafePurposeStat
import zip.cafe.entity.member.Member
import zip.cafe.entity.member.QMember.member
import zip.cafe.entity.review.Purpose
import zip.cafe.entity.review.QCafeKeyword.cafeKeyword
import zip.cafe.entity.review.QReviewCafeKeyword.reviewCafeKeyword
import zip.cafe.entity.review.ReviewCafeKeyword
import zip.cafe.util.Rectangle

@Repository
class SearchRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun searchByMemberNickname(query: String): List<Member> = queryFactory
        .select(member)
        .from(member)
        .where(member.nickname.contains(query))
        .fetch()

    fun searchCafe(
        cafeName: String,
        visitPurposeThreshold: Int,
        visitPurposeList: List<Purpose>,
        foodThreshold: Int,
        foodList: List<Food>,
        keywordNumberThreshold: Int,
        keywordIdList: List<Long>,
        boundary: Rectangle,
        minCafeId: Long?,
        limit: Long
    ): List<Cafe> = queryFactory
        .select(cafe).distinct()
        .from(cafe)
        .leftJoin(cafe.purposeStat, cafePurposeStat)
        .leftJoin(cafe.foodStat, cafeFoodStat)
        .leftJoin(cafe.keywordStat, cafeKeywordStat)
        .where(
            cafe.name.contains(cafeName)
                .and(checkVisitPurpose(visitPurposeThreshold, visitPurposeList))
                .and(checkFood(foodThreshold, foodList))
                .and(checkKeyword(keywordNumberThreshold, keywordIdList))
                .and(cafe.location.latitude.between(boundary.leftBottom.latitude, boundary.leftTop.latitude))
                .and(cafe.location.longitude.between(boundary.leftTop.longitude, boundary.rightTop.longitude))
                .and(checkMinCafeId(minCafeId))
        )
        .limit(limit)
        .fetch()

    private fun checkMinCafeId(minCafeId: Long?): BooleanExpression? = if (minCafeId == null) null else cafe.id.gt(minCafeId)

    private fun checkVisitPurpose(threshold: Int, visitPurposeList: List<Purpose>): BooleanExpression? {
        if (visitPurposeList.isEmpty()) return null
        return cafePurposeStat.purpose.`in`(visitPurposeList).and(cafePurposeStat.averageScore.average.goe(threshold))
    }

    private fun checkFood(threshold: Int, foodList: List<Food>): BooleanExpression? {
        if (foodList.isEmpty()) return null
        return cafeFoodStat.food.`in`(foodList) .and(cafeFoodStat.averageScore.average.goe(threshold))
    }

    private fun checkKeyword(threshold: Int, keywordIdList: List<Long>): BooleanExpression? {
        if (keywordIdList.isEmpty()) return null
        return cafeKeywordStat.keyword.id.`in`(keywordIdList).and(cafeKeywordStat.rank.loe(threshold))
    }

    fun searchByKeyword(keyword: String): List<ReviewCafeKeyword> = queryFactory
        .select(reviewCafeKeyword)
        .from(reviewCafeKeyword)
        .join(reviewCafeKeyword.cafeKeyword, cafeKeyword).fetchJoin()
        .join(reviewCafeKeyword.review).fetchJoin()
        .where(cafeKeyword.keyword.contains(keyword))
        .fetch()
}
