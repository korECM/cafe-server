package zip.cafe.config

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ofPattern


const val defaultDateTimeFormat = "yyyy-MM-dd HH:mm:ss"
const val defaultDateFormat = "yyyy-MM-dd"

fun LocalDateTime.formatAsDefault(): String = this.format(ofPattern(defaultDateTimeFormat))
fun LocalDate.formatAsDefault(): String = this.format(ofPattern(defaultDateFormat))
