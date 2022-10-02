package zip.cafe.entity

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Point(
    @Column(nullable = false)
    val latitude: Double,
    @Column(nullable = false)
    val longitude : Double
)
