package zip.cafe.entity.review

import zip.cafe.entity.common.BaseClass
import zip.cafe.entity.member.Member
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
class ReviewComment(
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    val review: Review,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,
    @Column(name = "content", nullable = false)
    val content: String
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_comment_id", nullable = false)
    val id: Long = 0
}
