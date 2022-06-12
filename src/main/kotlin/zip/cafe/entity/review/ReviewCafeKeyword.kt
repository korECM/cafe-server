package zip.cafe.entity.review

import org.hibernate.Hibernate
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Table(name = "REVIEW_CAFE_KEYWORD")
@Entity
class ReviewCafeKeyword(
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafe_keyword_id")
    val cafeKeyword: CafeKeyword,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    val review: Review
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_keyword_review_id", nullable = false)
    val id: Long = 0
}
