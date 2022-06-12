package zip.cafe.util

import org.locationtech.jts.geom.Point
import org.locationtech.jts.io.WKTReader

val seoulLatitude = 37.532600
val seoulLongitude = 127.024612

fun createPoint(longitude: Double, latitude: Double): Point {
    val pointWKT = String.format("POINT(%s %s)", longitude, latitude)
    return WKTReader().read(pointWKT) as Point
}
