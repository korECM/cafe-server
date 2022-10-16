package zip.cafe.entity.review

import zip.cafe.entity.FloatScore
import zip.cafe.entity.Food
import zip.cafe.entity.IntScore
import zip.cafe.entity.ReviewImage
import zip.cafe.entity.common.BaseClass
import zip.cafe.entity.member.Member
import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.FetchType.LAZY

@Entity
class Review protected constructor(
    @OneToOne(fetch = LAZY, optional = false, mappedBy = "review")
    val footprint: Footprint,

    @Embedded
    @Column(nullable = false)
    val finalScore: FloatScore,

    @Column(nullable = false)
    val description: String,
    _likeCount: Long = 0L,
    _commentCount: Long = 0L,
) : BaseClass() {
    companion object {
        fun from(footprint: Footprint, finalScore: FloatScore, description: String): Review =
            Review(footprint, finalScore, description).apply {
                footprint.review = this
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

    @OneToMany(mappedBy = "review", cascade = [ALL], orphanRemoval = true)
    private val _likes: MutableSet<ReviewLike> = mutableSetOf()
    val likers: List<Member>
        get() = _likes.map { it.member }

    @Column(nullable = false)
    var likeCount: Long = _likeCount

    @Column(nullable = false)
    var commentCount: Long = _commentCount

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
        likeCount += 1
    }

    fun removeLiker(member: Member) {
        val isRemoved = _likes.removeIf { it.member == member }
        if (isRemoved) {
            likeCount -= 1
        }
    }

    fun addImage(reviewImage: ReviewImage) {
        if (_images.contains(reviewImage)) {
            return
        }
        _images += reviewImage
        reviewImage.assignReview(this)
    }
}
