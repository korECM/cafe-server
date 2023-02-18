package zip.cafe.util

data class Rectangle(
    val leftTop: Point,
    val rightBottom: Point,
) {
    val rightTop: Point = Point(latitude = leftTop.latitude, longitude = rightBottom.longitude)
    val leftBottom: Point = Point(latitude = rightBottom.latitude, longitude = leftTop.longitude)

    val center: Point = Point(
        (leftTop.latitude + rightTop.latitude + rightBottom.latitude + leftBottom.latitude) / 4,
        (leftTop.longitude + rightTop.longitude + rightBottom.longitude + leftBottom.longitude) / 4
    )

    fun contain(point: Point) = point.latitude in leftBottom.latitude..leftTop.latitude && point.longitude in leftTop.longitude..rightTop.longitude
    fun contain(latitude: Double, longitude: Double) = contain(Point(latitude, longitude))
}
