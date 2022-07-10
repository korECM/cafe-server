package zip.cafe.entity.cafe

import org.hibernate.Hibernate
import zip.cafe.entity.common.BaseClass
import zip.cafe.entity.menu.Menu
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Table(name = "cafe_menu")
@Entity
class CafeMenu(
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    val cafe: Cafe,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    val menu: Menu
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cafe_menu_id", nullable = false)
    val id: Long = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CafeMenu

        return menu == other.menu && cafe == other.cafe
    }

    override fun hashCode() = 31 * menu.hashCode() + cafe.hashCode()
}
