package zip.cafe.utils

import io.github.serpro69.kfaker.faker

fun createFaker() = faker {
    fakerConfig {
        locale = "ko"
    }
}

val faker = createFaker()
