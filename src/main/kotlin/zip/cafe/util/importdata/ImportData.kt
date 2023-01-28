package zip.cafe.util.importdata

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import zip.cafe.entity.Point
import zip.cafe.entity.cafe.Cafe
import zip.cafe.util.createPoint
import zip.cafe.util.importdata.item.ImportDataItem
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
    fun importData() {
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

        println("import data")
        importService.importData(dataList)
    }

    @Component
    @Transactional
    class ImportService(
        private val em: EntityManager,
    ) {
        fun importData(dataList: List<ImportDataItem>) {
            dataList.forEach { data ->
                createCafe(
                    name = data.name,
                    address = data.fullAddress,
                    location = createPoint(data.latitude, data.longitude),
                    openingHours = data.businessHours
                )
            }
        }

        private fun createCafe(name: String, address: String, location: Point, openingHours: String): Cafe {
            val cafe = Cafe(name = name, address = address, location = location, openingHours = openingHours)
            em.persist(cafe)
            return cafe
        }
    }
}
