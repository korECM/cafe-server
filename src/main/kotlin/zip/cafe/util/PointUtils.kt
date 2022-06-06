package zip.cafe.util

import org.locationtech.jts.geom.Point
import org.locationtech.jts.io.WKTReader

fun createPoint(longitude: Double, latitude: Double): Point {
    val pointWKT = String.format("POINT(%s %s)", longitude, latitude)
    return WKTReader().read(pointWKT) as Point
}
