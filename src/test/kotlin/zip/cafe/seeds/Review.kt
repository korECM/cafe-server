package zip.cafe.seeds

import zip.cafe.entity.FloatScore
import zip.cafe.entity.IntScore
import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.member.Member
import zip.cafe.entity.review.Footprint
import zip.cafe.entity.review.Purpose
import zip.cafe.entity.review.Review
import zip.cafe.utils.faker
import zip.cafe.utils.newEntityId
import zip.cafe.utils.setEntityId
import java.time.LocalDate

fun createReview(
    id: Long = faker.newEntityId(),
    cafeId: Long = faker.newEntityId(),
    cafe: Cafe = createCafe(id = cafeId),
    memberId: Long = faker.newEntityId(),
    member: Member = createMember(id = memberId),
    visitPurpose: Purpose = faker.random.nextEnum(Purpose::class.java),
    visitPurposeScore: IntScore = createIntScore(),
    footprintId: Long = faker.newEntityId(),
    visitDate: LocalDate = LocalDate.now().minusDays(faker.random.nextLong(365)),
    footprint: Footprint = createFootprint(id = footprintId, cafeId = cafeId, cafe = cafe, memberId = memberId, member = member, visitDate = visitDate),
    finalScore: FloatScore = createFloatScore(),
    description: String = faker.quote.fortuneCookie()
) = setEntityId(
    id,
    Review.from(
        footprint = footprint,
        finalScore = finalScore,
        visitPurpose = visitPurpose,
        visitPurposeScore = visitPurposeScore,
        description = description
    )
)
