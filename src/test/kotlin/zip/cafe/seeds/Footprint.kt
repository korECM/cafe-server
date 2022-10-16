package zip.cafe.seeds

import zip.cafe.entity.cafe.Cafe
import zip.cafe.entity.member.Member
import zip.cafe.entity.review.Footprint
import zip.cafe.utils.faker
import zip.cafe.utils.newEntityId
import zip.cafe.utils.setEntityId
import java.time.LocalDate
import java.time.LocalDate.now

fun createFootprint(
    id: Long = faker.newEntityId(),
    cafeId: Long = faker.newEntityId(),
    cafe: Cafe = createCafe(id = cafeId),
    memberId: Long = faker.newEntityId(),
    member: Member = createMember(id = memberId),
    visitDate: LocalDate = now().minusDays(faker.random.nextLong(365))
) = setEntityId(
    id,
    Footprint.from(
        cafe = cafe,
        member = member,
        visitDate = visitDate
    )
)
