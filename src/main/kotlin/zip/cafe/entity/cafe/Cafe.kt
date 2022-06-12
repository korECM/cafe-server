package zip.cafe.entity.cafe

import org.locationtech.jts.geom.Point
import zip.cafe.entity.common.BaseClass
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id

@Entity
class Cafe(
    @Column(name = "name", nullable = false)
    val name: String,
    @Column(name = "address", nullable = false)
    val address: String,
    @Column(name = "location", nullable = false)
    val location: Point
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cafe_id", nullable = false)
    val id: Long = 0
}
