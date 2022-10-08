package zip.cafe.api.dto

import java.time.LocalDate
import javax.validation.constraints.NotNull

data class FootPrintRegisterRequest(
    @field:NotNull(message = "카페 ID가 없습니다")
    val cafeId: Long,
    @field:NotNull(message = "방문 날짜가 없습니다")
    val visitDate: LocalDate,
)

data class FootPrintRegisterResponse(
    val footprintId: Long
)
