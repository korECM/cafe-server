package zip.cafe.seeds

import zip.cafe.entity.IntScore
import zip.cafe.utils.faker

fun createIntScore(
    score: Int = faker.random.nextInt(1, 5)
) = IntScore(score = score)
