package study.cafe.util

import java.util.*

infix operator fun Date.plus(millisecond: Long): Date {
    return Date(this.time + millisecond)
}
