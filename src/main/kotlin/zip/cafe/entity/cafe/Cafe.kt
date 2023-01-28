package zip.cafe.entity.cafe

import zip.cafe.entity.Food
import zip.cafe.entity.IntScore
import zip.cafe.entity.Point
import zip.cafe.entity.common.BaseClass
import zip.cafe.entity.menu.Menu
import zip.cafe.entity.review.CafeKeyword
import zip.cafe.entity.review.Purpose
import zip.cafe.entity.review.Review
import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.GenerationType.IDENTITY

@Entity
class Cafe(
    @Column(name = "name", nullable = false)
    val name: String,
    @Column(name = "address", nullable = false)
    val address: String,
    @Column(name = "location", nullable = false)
    @Embedded
    val location: Point,
    @Column(name = "opening_hours", nullable = false, length = 500)
    val openingHours: String,

    _totalScore: Double = 0.0,
    _reviewCount: Long = 0L,
    _footPrintCount: Long = 0L,
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cafe_id", nullable = false)
    val id: Long = 0

    @OneToMany(mappedBy = "cafe", cascade = [ALL])
    private val _menus: MutableSet<CafeMenu> = mutableSetOf()
    val menus: List<Menu>
        get() = _menus.map { it.menu }

    @OneToMany(mappedBy = "cafe", cascade = [ALL])
    val purposeStat: MutableList<CafePurposeStat> = mutableListOf()

    @OneToMany(mappedBy = "cafe", cascade = [ALL])
    val foodStat: MutableList<CafeFoodStat> = mutableListOf()

    @OneToMany(mappedBy = "cafe", cascade = [ALL])
    val keywordStat: MutableList<CafeKeywordStat> = mutableListOf()

    @Column(nullable = false)
    var totalScore: Double = _totalScore
        protected set

    @Column(nullable = false)
    var reviewCount: Long = _reviewCount
        protected set

    @Column(nullable = false)
    var footPrintCount: Long = _footPrintCount
        protected set

    fun addMenu(menu: Menu) {
        _menus += CafeMenu(cafe = this, menu = menu)
    }

    fun incReviewCountAndCalculateAverageScore(review: Review) {
        reviewCount += 1
        totalScore += review.finalScore.score
    }

    fun incFootPrintCount() {
        footPrintCount += 1
    }

    fun addPurposeStat(purpose: Purpose, score: IntScore) {
        purposeStat += CafePurposeStat(cafe = this, purpose = purpose, count = 1, totalScore = score.score.toLong())
    }

    fun addFoodStat(food: Food, score: IntScore) {
        foodStat += CafeFoodStat(cafe = this, food = food, count = 1, totalScore = score.score.toLong())
    }

    fun addKeywordStat(keyword: CafeKeyword) {
        keywordStat += CafeKeywordStat(cafe = this, keyword = keyword, _count = 1)
    }
}
