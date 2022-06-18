package zip.cafe.entity.review

import zip.cafe.entity.member.Member
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Table(name = "REVIEW_LIKE")
@Entity
class ReviewLike(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    val review: Review,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member
) {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "review_like_id", nullable = false)
    val id: Long = 0
}
