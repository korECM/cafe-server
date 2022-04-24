package study.cafe.entity

import study.cafe.entity.common.BaseClass
import study.cafe.entity.member.Member
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
    val description: String
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    val id: Long = 0

    @OneToMany(mappedBy = "review", cascade = [ALL])
    @Column(nullable = false)
    val visitPurposeInfo: MutableList<VisitPurposeInformation> = mutableListOf()

    @OneToMany(mappedBy = "review", cascade = [ALL])
    @Column(nullable = false)
    val foodInfos: MutableList<ReviewFoodInfo> = mutableListOf()

    @OneToMany(mappedBy = "review", cascade = [ALL])
    @Column(nullable = false)
    val cafeKeywords: MutableList<ReviewCafeKeyword> = mutableListOf()

    fun addVisitPurposeInfo(purpose: Purpose, score: IntScore) {
        visitPurposeInfo += VisitPurposeInformation(purpose, score, this)
    }

    fun addFoodInfo(food: Food, score: IntScore) {
        foodInfos += ReviewFoodInfo(food, score, this)
    }

    fun addCafeKeyword(cafeKeyword: CafeKeyword) {
        cafeKeywords += ReviewCafeKeyword(cafeKeyword, this)
    }
}
