package zip.cafe.entity.review

import zip.cafe.entity.FloatScore
import zip.cafe.entity.Food
import zip.cafe.entity.IntScore
import zip.cafe.entity.ReviewImage
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.common.BaseClass
import zip.cafe.entity.member.Member
import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.FetchType.LAZY

@Entity
class Review(
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    val cafe: Cafe,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @Embedded
    @Column(nullable = false)
    val finalScore: FloatScore,

    @Column(nullable = false)
    val description: String,

    likeCount: Long = 0
) : BaseClass() {
    companion object {
        fun from(cafe: Cafe, member: Member, finalScore: FloatScore, description: String): Review {
            val review = Review(cafe, member, finalScore, description)
//            cafe.addReview(review)
            return review
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    val id: Long = 0

    @OneToMany(mappedBy = "review", cascade = [ALL])
    @Column(nullable = false)
    private val _visitPurposeInfo: MutableList<VisitPurposeInformation> = mutableListOf()
    val visitPurposeInfo: List<VisitPurposeInformation>
        get() = _visitPurposeInfo

    @OneToMany(mappedBy = "review", cascade = [ALL])
    @Column(nullable = false)
    private val _foodInfos: MutableSet<ReviewFoodInfo> = mutableSetOf()
    val foodInfos: Set<ReviewFoodInfo>
        get() = _foodInfos

    @OneToMany(mappedBy = "review", cascade = [ALL])
    @Column(nullable = false)
    private val _cafeKeywords: MutableList<ReviewCafeKeyword> = mutableListOf()
    val cafeKeywords: List<ReviewCafeKeyword>
        get() = _cafeKeywords

    @OneToMany(mappedBy = "review", cascade = [ALL])
    @Column(nullable = false)
    private val _images: MutableList<ReviewImage> = mutableListOf()
    val images: List<ReviewImage>
        get() = _images

    fun addVisitPurposeInfo(purpose: Purpose, score: IntScore) {
        _visitPurposeInfo += VisitPurposeInformation(purpose, score, this)
    }

    fun addFoodInfo(food: Food, score: IntScore) {
        _foodInfos += ReviewFoodInfo(food, score, this)
    }

    fun addCafeKeyword(cafeKeyword: CafeKeyword) {
        _cafeKeywords += ReviewCafeKeyword(cafeKeyword, this)
    }
}
