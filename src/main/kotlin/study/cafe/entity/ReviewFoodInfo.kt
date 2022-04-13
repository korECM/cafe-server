package study.cafe.entity

import study.cafe.entity.common.BaseClass
import javax.persistence.*
import javax.persistence.EnumType.STRING
import javax.persistence.FetchType.LAZY

@Entity
class ReviewFoodInfo(
    @Enumerated(STRING)
    @Column(nullable = false)
    val food: Food,
    @Embedded
    @Column(nullable = false)
    val score: SatisfactionScore,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    val review: Review
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_food_info_id", nullable = false)
    val id: Long = 0
}
