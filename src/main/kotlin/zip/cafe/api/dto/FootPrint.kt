package zip.cafe.api.dto

import javax.validation.constraints.NotNull

data class FootPrintRegisterRequest(
    @field:NotNull(message = "카페 ID가 없습니다")
    val cafeId: Long,

    ) {
}
