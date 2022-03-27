package study.cafe.entity

import study.cafe.entity.common.BaseClass
import javax.persistence.*

@Entity
class Review : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    val id: Long = 0
}
