package zip.cafe.util

data class Point(
    val latitude: Double,
    val longitude: Double,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false

        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false

        return true
    }

    override fun hashCode(): Int {
        var result = latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }

    operator fun plus(other: Point): Point = Point(latitude = latitude + other.latitude, longitude = longitude + other.longitude)
    operator fun minus(other: Point): Point = Point(latitude = latitude - other.latitude, longitude = longitude - other.longitude)
}
