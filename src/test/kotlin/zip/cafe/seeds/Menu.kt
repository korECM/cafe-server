package zip.cafe.seeds

import zip.cafe.entity.menu.Menu
import zip.cafe.utils.faker
import zip.cafe.utils.newEntityId
import zip.cafe.utils.setEntityId

fun createMenu(
    id: Long = faker.newEntityId(),
    name: String = faker.dessert.variety(),
    price: Long = faker.random.nextLong(5000) + 2000
) = setEntityId(
    id,
    Menu(
        name = name,
        price = price
    )
)
