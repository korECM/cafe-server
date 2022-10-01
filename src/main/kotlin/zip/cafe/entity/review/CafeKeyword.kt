package zip.cafe.entity.review

import zip.cafe.entity.common.BaseClass
import javax.persistence.*

@Table(name = "cafe_keyword")
@Entity
class CafeKeyword(
    val keyword: String,
    val emoji: String
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_keyword_id", nullable = false)
    val id: Long = 0
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CafeKeyword

        if (keyword != other.keyword) return false
        if (emoji != other.emoji) return false

        return true
    }

    override fun hashCode() = 31 * keyword.hashCode() + emoji.hashCode()
}
