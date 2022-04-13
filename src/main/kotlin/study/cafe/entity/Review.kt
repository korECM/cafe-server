package study.cafe.entity

import study.cafe.entity.common.BaseClass
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
class Review(
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    val cafe: Cafe,

    @Column(nullable = false)
    val finalScore: Double,

    @Column(nullable = false)
    val description: String
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    val id: Long = 0

    @OneToMany(mappedBy = "review")
    @Column(nullable = false)
    val visitPurposeInfo: List<VisitPurposeInformation> = listOf()

    @OneToMany(mappedBy = "review")
    @Column(nullable = false)
    val foodInfos: List<ReviewFoodInfo> = listOf()

    @OneToMany(mappedBy = "review")
    @Column(nullable = false)
    val cafeKeywords: List<ReviewCafeKeyword> = listOf()
}
