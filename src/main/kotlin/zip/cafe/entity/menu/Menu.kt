package zip.cafe.entity.menu

import zip.cafe.entity.common.BaseClass
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Table(name = "menu")
@Entity
class Menu(
    @Column(name = "name", nullable = false)
    val name: String,
    @Column(name = "price", nullable = false)
    val price: Long,
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "menu_id", nullable = false)
    val id: Long = 0
}
