package study.cafe.entity

import study.cafe.entity.common.BaseClass
import java.time.LocalDate
import javax.persistence.*
import javax.persistence.EnumType.STRING
import javax.persistence.GenerationType.IDENTITY

@Entity
class Member(
    @Column(nullable = false)
    val nickName: String,

    @Column(nullable = false)
    var birthDay: LocalDate,

    @Enumerated(STRING)
    @Column(nullable = false)
    var gender: Gender
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id", nullable = false)
    val id: Long = 0
}
