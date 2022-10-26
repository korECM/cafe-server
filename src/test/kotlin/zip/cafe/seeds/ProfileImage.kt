package zip.cafe.seeds

import zip.cafe.entity.ProfileImage
import zip.cafe.entity.member.Member
import zip.cafe.utils.faker
import zip.cafe.utils.newEntityId
import zip.cafe.utils.setEntityId

fun createProfileImage(
    id: Long = faker.newEntityId(),
    bucket: String = faker.random.randomString(5, false),
    fileKey: String = faker.random.randomString(3, false),
    s3URL: String = faker.internet.domain(),
    cloudFrontURL: String = faker.internet.domain(),
    uploaderId: Long = faker.newEntityId(),
    uploadedBy: Member = createMember(id = uploaderId)
) = setEntityId(
    id,
    ProfileImage.withoutMember(
        bucket = bucket,
        fileKey = fileKey,
        s3URL = s3URL,
        cloudFrontURL = cloudFrontURL,
        uploadedBy = uploadedBy
    )
)

fun createProfileImageWithMember(
    id: Long = faker.newEntityId(),
    bucket: String = faker.random.randomString(5, false),
    fileKey: String = faker.random.randomString(3, false),
    s3URL: String = faker.internet.domain(),
    cloudFrontURL: String = faker.internet.domain(),
    uploaderId: Long = faker.newEntityId(),
    uploadedBy: Member = createMember(id = uploaderId),
    memberId: Long = faker.newEntityId(),
    member: Member = createMember(id = memberId)
) = setEntityId(
    id,
    ProfileImage.from(
        bucket = bucket, fileKey = fileKey, s3URL = s3URL, cloudFrontURL = cloudFrontURL, uploadedBy = uploadedBy, member = member
    )
)
