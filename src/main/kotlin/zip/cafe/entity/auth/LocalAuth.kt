package zip.cafe.entity.auth

import zip.cafe.entity.common.BaseClass
import zip.cafe.entity.member.Member
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Table(name = "local_auth")
@Entity
class LocalAuth(
    @Column(nullable = false)
    var localId: String,

    @Column(nullable = false)
    var localPassword: String,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "email_auth_id", nullable = false)
    val id: Long = 0

    @Column(nullable = false)
    var isDeleted: Boolean = false
        protected set

    var deletedAt: LocalDateTime? = null
        protected set

    fun delete() {
        this.isDeleted = true
        this.deletedAt = LocalDateTime.now()
    }
}
