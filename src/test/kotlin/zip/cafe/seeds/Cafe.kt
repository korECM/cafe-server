package zip.cafe.seeds

import org.locationtech.jts.geom.Point
import zip.cafe.entity.cafe.Cafe
import zip.cafe.util.createPoint
import zip.cafe.util.seoulLatitude
import zip.cafe.util.seoulLongitude
import zip.cafe.utils.faker
import zip.cafe.utils.setEntityId

fun createCafe(
    id: Long = faker.random.nextLong(),
    name: String = faker.name.name(),
    address: String = faker.address.cityWithState(),
    location: Point = createPoint(seoulLongitude + faker.random.nextDouble() / 2, seoulLatitude + faker.random.nextDouble() / 2)
) = setEntityId(
    id,
    Cafe(
        name = name,
        address = address,
        location = location,
    )
)
