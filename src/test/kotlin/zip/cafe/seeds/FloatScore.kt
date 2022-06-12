package zip.cafe.seeds

import zip.cafe.entity.FloatScore
import zip.cafe.utils.faker

fun createFloatScore(
    score: Double = faker.random.nextInt(1, 10) * 0.5
) = FloatScore(score = score)
