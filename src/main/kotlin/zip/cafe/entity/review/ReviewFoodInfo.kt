package zip.cafe.entity.review

import org.hibernate.Hibernate
import zip.cafe.entity.Food
import zip.cafe.entity.IntScore
import zip.cafe.entity.common.BaseClass
import javax.persistence.*
import javax.persistence.EnumType.STRING
import javax.persistence.FetchType.LAZY

@Table(name = "REVIEW_FOOD_INFO")
@Entity
class ReviewFoodInfo(
    @Enumerated(STRING)
    @Column(nullable = false)
    val food: Food,
    @Embedded
    @Column(nullable = false)
    val score: IntScore,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    val review: Review
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_food_info_id", nullable = false)
    val id: Long = 0
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ReviewFoodInfo

        return review == other.review && food == other.food
    }

    override fun hashCode() = 31 * food.hashCode() + review.hashCode()
}
