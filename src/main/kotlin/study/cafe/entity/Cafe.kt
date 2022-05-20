package study.cafe.entity

import org.locationtech.jts.geom.Point
import study.cafe.entity.common.BaseClass
import study.cafe.entity.member.Member
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id

@Entity
class Cafe(
    @Column(name = "name", nullable = false)
    val name: String,
    @Column(name = "address", nullable = false)
    val address: String,
    @Column(name = "location", nullable = false)
    val location: Point,
    @Column(name = "totalScore", nullable = false)
    var totalScore: Double = 0.0,
    @Column(name = "reviewCount", nullable = false)
    var reviewCount: Long = 0,
    likeCount: Long = 0
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cafe_id", nullable = false)
    val id: Long = 0

    @Column(name = "like_count", nullable = false)
    var likeCount: Long = likeCount
        private set

    val averageScore: Double
        get() = if (reviewCount == 0L) 0.0 else totalScore / reviewCount

    fun addReview(review: Review) {
        this.reviewCount += 1
        this.totalScore += review.finalScore.score
    }

    fun addLiker(member: Member) {
        this.likeCount += 1
    }
}
