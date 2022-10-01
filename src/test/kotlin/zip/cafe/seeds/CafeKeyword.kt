package zip.cafe.seeds

import zip.cafe.entity.review.CafeKeyword
import zip.cafe.utils.faker
import zip.cafe.utils.newEntityId
import zip.cafe.utils.setEntityId

fun createCafeKeyword(
    id: Long = faker.newEntityId(),
    keyword: String = listOf("아늑한", "조용한").random(),
    emoji: String = listOf("😃", "🧑").random()
) = setEntityId(
    id,
    CafeKeyword(
        keyword = keyword,
        emoji = emoji
    )
)
