package zip.cafe.seeds

import org.locationtech.jts.geom.Point
import zip.cafe.entity.cafe.Cafe
import zip.cafe.util.createPoint
import zip.cafe.util.seoulLatitude
import zip.cafe.util.seoulLongitude
import zip.cafe.utils.faker
import zip.cafe.utils.newEntityId
import zip.cafe.utils.setEntityId

val openingHoursList = listOf(
    "주중 : 09:00~18:00\n주말 : 10:00~15:00",
    "주중 : 07:00~21:00\n주말 : 08:00~20:00",
    "월~금 08:00 ~ 22:00\n토,일 10:00 ~ 22:00",
    "08:00 ~ 22:00",
)

fun createCafe(
    id: Long = faker.newEntityId(),
    name: String = faker.name.name(),
    address: String = faker.address.cityWithState(),
    location: Point = createPoint(seoulLongitude + faker.random.nextDouble() / 2, seoulLatitude + faker.random.nextDouble() / 2),
    openingHours: String = openingHoursList.random()
) = setEntityId(
    id,
    Cafe(
        name = name,
        address = address,
        location = location,
        openingHours = openingHours,
    )
)
