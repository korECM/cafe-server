package zip.cafe.util.importdata

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import zip.cafe.entity.Point
import zip.cafe.entity.cafe.Cafe
import zip.cafe.util.createPoint
import zip.cafe.util.importdata.item.ImportDataItem
import zip.cafe.util.logger
import java.io.FileReader
import java.io.Reader
import javax.annotation.PostConstruct
import javax.persistence.EntityManager

@Profile("import")
@Component
class ImportData(
    private val importService: ImportService,
) {

    @PostConstruct
    fun importDataFn() {
        val objectMapper = jacksonObjectMapper()
        val reader: Reader = FileReader("/Users/korecm/Programming/Study/cafe-server/cafe/data.json")

        val tree = objectMapper.readTree(reader)
        val dataList = mutableListOf<ImportDataItem>()

        tree.elements().forEach {
            val data = objectMapper.treeToValue(it, ImportDataItem::class.java)
            if (data.id != 0) {
                dataList += data
            }
        }
        importService.importData(dataList)
    }

    @Component
    @Transactional
    class ImportService(
        private val em: EntityManager,
    ) {
        fun importData(dataList: List<ImportDataItem>) {
            val chunked = dataList.chunked(100)
            val numberOfChunk = chunked.size
            chunked.forEachIndexed { index, data ->
                logger().info("Importing data: $index / $numberOfChunk")
                data.forEach {
                    createCafe(
                        name = it.name,
                        address = it.fullAddress,
                        location = createPoint(longitude = it.longitude, latitude = it.latitude),
                        openingHours = it.businessHours
                    )
                }
            }
        }

        private fun createCafe(name: String, address: String, location: Point, openingHours: String): Cafe {
            val cafe = Cafe(name = name, address = address, location = location, openingHours = openingHours)
            em.persist(cafe)
            return cafe
        }
    }
}
