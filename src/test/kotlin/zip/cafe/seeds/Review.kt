package zip.cafe.seeds

import zip.cafe.entity.FloatScore
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.member.Member
import zip.cafe.entity.review.Review
import zip.cafe.utils.faker
import zip.cafe.utils.newEntityId
import zip.cafe.utils.setEntityId

fun createReview(
    id: Long = faker.newEntityId(),
    cafeId: Long = faker.newEntityId(),
    cafe: Cafe = createCafe(id = cafeId),
    memberId: Long = faker.newEntityId(),
    member: Member = createMember(id = memberId),
    finalScore: FloatScore = createFloatScore(),
    description: String = faker.quote.fortuneCookie()
) = setEntityId(
    id,
    Review(
        cafe = cafe,
        member = member,
        finalScore = finalScore,
        description = description
    )
)
