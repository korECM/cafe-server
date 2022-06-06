package zip.cafe.seeds

import zip.cafe.entity.member.Gender
import zip.cafe.entity.member.Member
import zip.cafe.utils.faker
import java.time.LocalDate

fun createMember(
    name: String = faker.name.name(),
    nickname: String = faker.name.name(),
    birthDay: LocalDate = faker.person.birthDate(faker.random.nextInt(10, 50).toLong()),
    gender: Gender = faker.random.nextEnum(Gender::class.java)
): Member = Member(
    name = name,
    nickname = nickname,
    birthDay = birthDay,
    gender = gender
)
