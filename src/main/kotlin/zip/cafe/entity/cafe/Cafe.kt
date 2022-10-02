package zip.cafe.entity.cafe

import zip.cafe.entity.Point
import zip.cafe.entity.common.BaseClass
import zip.cafe.entity.menu.Menu
import javax.persistence.*
import javax.persistence.CascadeType.ALL
import javax.persistence.GenerationType.IDENTITY

@Entity
class Cafe(
    @Column(name = "name", nullable = false)
    val name: String,
    @Column(name = "address", nullable = false)
    val address: String,
    @Column(name = "location", nullable = false)
    @Embedded
    val location: Point,
    @Column(name = "opening_hours", nullable = false)
    val openingHours: String
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cafe_id", nullable = false)
    val id: Long = 0

    @OneToMany(mappedBy = "cafe", cascade = [ALL])
    private val _menus: MutableSet<CafeMenu> = mutableSetOf()
    val menus: List<Menu>
        get() = _menus.map { it.menu }

    fun addMenu(menu: Menu) {
        _menus += CafeMenu(cafe = this, menu = menu)
    }
}
