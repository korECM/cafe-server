package study.cafe.entity

import study.cafe.entity.common.BaseClass
import study.cafe.entity.member.Member
import javax.persistence.*
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
    val description: String
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    val id: Long = 0

    @OneToMany(mappedBy = "review")
    @Column(nullable = false)
    val visitPurposeInfo: List<VisitPurposeInformation> = mutableListOf()

    @OneToMany(mappedBy = "review")
    @Column(nullable = false)
    val foodInfos: List<ReviewFoodInfo> = mutableListOf()

    @OneToMany(mappedBy = "review")
    @Column(nullable = false)
    val cafeKeywords: List<ReviewCafeKeyword> = mutableListOf()

    fun addVisitPurposeInfo(purpose: Purpose, score: SatisfactionScore) {
        val info = VisitPurposeInformation(purpose, score, this)
        visitPurposeInfo + info
    }

    fun addFoodInfo(food: Food, score: SatisfactionScore) {
        val info = ReviewFoodInfo(food, score, this)
        foodInfos + info
    }

    fun addCafeKeyword(cafeKeyword: CafeKeyword) {
        val reviewCafeKeyword = ReviewCafeKeyword(cafeKeyword, this)
        cafeKeywords + reviewCafeKeyword
    }
}
