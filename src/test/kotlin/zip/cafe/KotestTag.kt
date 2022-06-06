package zip.cafe

import io.kotest.core.NamedTag

const val integrationTestTag = "IntegrationTest"

fun String.tag() = NamedTag(this)
