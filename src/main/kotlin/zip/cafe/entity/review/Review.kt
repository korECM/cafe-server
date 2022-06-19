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
    private val _visitPurposeInfo: MutableSet<VisitPurposeInformation> = mutableSetOf()
    val visitPurposeInfo: Set<VisitPurposeInformation>
        get() = _visitPurposeInfo

    @OneToMany(mappedBy = "review", cascade = [ALL])
    private val _foodInfos: MutableSet<ReviewFoodInfo> = mutableSetOf()
    val foodInfos: Set<ReviewFoodInfo>
        get() = _foodInfos

    @OneToMany(mappedBy = "review", cascade = [ALL])
    private val _cafeKeywords: MutableSet<ReviewCafeKeyword> = mutableSetOf()
    val cafeKeywords: List<CafeKeyword>
        get() = _cafeKeywords.map { it.cafeKeyword }

    @OneToMany(mappedBy = "review", cascade = [ALL])
    private val _images: MutableList<ReviewImage> = mutableListOf()
    val images: List<ReviewImage>
        get() = _images

    @OneToMany(mappedBy = "", cascade = [ALL], orphanRemoval = true)
    private val _likes: MutableSet<ReviewLike> = mutableSetOf()
    val likers: List<Member>
        get() = _likes.map { it.member }

    fun addVisitPurposeInfo(purpose: Purpose, score: IntScore) {
        _visitPurposeInfo += VisitPurposeInformation(purpose, score, this)
    }

    fun addFoodInfo(food: Food, score: IntScore) {
        _foodInfos += ReviewFoodInfo(food, score, this)
    }

    fun addCafeKeyword(cafeKeyword: CafeKeyword) {
        _cafeKeywords += ReviewCafeKeyword(cafeKeyword, this)
    }

    fun addLiker(member: Member) {
        _likes += ReviewLike(review = this, member = member)
    }

    fun removeLiker(member: Member) {
        _likes.removeIf { it.member == member }
    }
}
