package zip.cafe.seeds

import zip.cafe.entity.member.Member
import zip.cafe.utils.faker
import zip.cafe.utils.newEntityId
import zip.cafe.utils.setEntityId

val MOCK_MVC_USER_ID = faker.newEntityId()
fun createMember(
    id: Long = faker.newEntityId(),
    nickname: String = faker.name.name(),
    profileImageURL: String = Member.DEFAULT_PROFILE_IMAGE_URL
): Member = setEntityId(
    id,
    Member(
        nickname = nickname,
        profileImage = profileImageURL,
    )
)
