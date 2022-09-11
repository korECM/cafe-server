package zip.cafe.entity.auth

import zip.cafe.entity.common.BaseClass
import zip.cafe.entity.member.Member
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Table(name = "apple_auth")
@Entity
class AppleAuth(
    @Column(nullable = false)
    var appleId: Long,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "apple_auth_id", nullable = false)
    val id: Long = 0
}