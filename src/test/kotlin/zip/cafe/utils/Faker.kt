package zip.cafe.utils

import io.github.serpro69.kfaker.Faker
import io.github.serpro69.kfaker.faker

fun createFaker() = faker {
    fakerConfig {
        locale = "ko"
    }
}

val faker = createFaker()

fun Faker.newEntityId() = this.random.nextLong(123456789)
