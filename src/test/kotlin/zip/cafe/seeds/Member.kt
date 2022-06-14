package zip.cafe.seeds

import zip.cafe.entity.member.Gender
import zip.cafe.entity.member.Member
import zip.cafe.utils.faker
import zip.cafe.utils.setEntityId
import java.time.LocalDate

val MOCK_MVC_USER_ID = faker.random.nextLong()
fun createMember(
    id: Long = faker.random.nextLong(),
    name: String = faker.name.name(),
    nickname: String = faker.name.name(),
    birthDay: LocalDate = faker.person.birthDate(faker.random.nextInt(10, 50).toLong()),
    gender: Gender = faker.random.nextEnum(Gender::class.java)
): Member = setEntityId(
    id,
    Member(
        name = name,
        nickname = nickname,
        birthDay = birthDay,
        gender = gender
    )
)
