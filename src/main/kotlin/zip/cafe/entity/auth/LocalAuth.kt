package zip.cafe.entity.auth

import zip.cafe.entity.common.BaseClass
import zip.cafe.entity.member.Member
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

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
}
