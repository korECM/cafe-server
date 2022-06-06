package zip.cafe.entity.review

import zip.cafe.entity.IntScore
import zip.cafe.entity.common.BaseClass
import javax.persistence.*
import javax.persistence.EnumType.STRING

@Entity
class VisitPurposeInformation(
    @Enumerated(STRING)
    @Column(nullable = false)
    val visitPurpose: Purpose,

    @Embedded
    @Column(nullable = false)
    val score: IntScore,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    val review: Review
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_visit_purpose_id", nullable = false)
    val id: Long = 0
}
