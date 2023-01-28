package zip.cafe.util.importdata.item

data class ImportDataItem(
    val businessHours: String,
    val category: String,
    val createdAt: Long,
    val fullAddress: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val phoneNumber: String,
    val shortAddress: String,
    val updatedAt: Long
)
