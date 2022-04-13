package study.cafe.entity

import study.cafe.entity.common.BaseClass
import javax.persistence.*

@Entity
class CafeKeyword(
    val keyword: String
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_keyword_id", nullable = false)
    val id: Long = 0
}
