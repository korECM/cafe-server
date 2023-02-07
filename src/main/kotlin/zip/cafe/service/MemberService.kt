package zip.cafe.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import zip.cafe.connector.S3Connector
import zip.cafe.connector.dto.S3FileDto
import zip.cafe.entity.ProfileImage
import zip.cafe.entity.member.Member
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.ProfileImageRepository
import zip.cafe.repository.findOneById

private const val DEFAULT_PROFILE_IMAGE_URL = "https://d125lzs083ehp3.cloudfront.net/default.png"

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val profileImageRepository: ProfileImageRepository,
    private val s3Connector: S3Connector,
    @Value("\${cloud.aws.s3.review-image-bucket}")
    private val reviewImageBucket: String,
) {
    fun checkNicknameDuplication(memberId: Long, nickname: String): Boolean {
        val nicknameOwner = memberRepository.findByNickname(nickname) ?: return false
        return nicknameOwner.id != memberId
    }

    fun findMemberById(memberId: Long) = memberRepository.findOneById(memberId)

    @Transactional
    fun initMemberProfile(memberId: Long, nickname: String, description: String, profileImageId: Long?) {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("존재하지 않는 회원입니다")
        require(!member.isProfileInit) { "이미 프로필을 초기 설정한 회원입니다" }
        applyProfile(member, nickname, description, profileImageId)
        member.isProfileInit = true
    }

    @Transactional
    fun editMemberProfile(memberId: Long, nickname: String, description: String, profileImageId: Long?) {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("존재하지 않는 회원입니다")
        applyProfile(member, nickname, description, profileImageId)
    }

    private fun applyProfile(member: Member, nickname: String, description: String, profileImageId: Long?) {
        require(!checkNicknameDuplication(member.id, nickname)) { "중복되는 닉네임입니다" }

        val profileImageURL = if (profileImageId != null) {
            val profileImage = profileImageRepository.findByIdOrNull(profileImageId) ?: throw IllegalArgumentException("존재하지 않는 프로필 이미지입니다")
            require(profileImage.uploadedBy.id == member.id) { "본인의 프로필 이미지만 사용할 수 있습니다" }
            profileImage.member = member
            profileImage.cloudFrontURL
        } else {
            DEFAULT_PROFILE_IMAGE_URL
        }

        member.profileImage = profileImageURL
        member.nickname = nickname
        member.description = description
    }

    @Transactional(propagation = Propagation.NEVER)
    fun uploadProfileImage(image: MultipartFile): S3FileDto {
        // TODO MaxUploadSizeExceededException 예외처리
        return s3Connector.uploadFile(bucketName = reviewImageBucket, dirName = "org", multipartFile = image)
    }

    @Transactional
    fun saveUploadedProfileImage(uploadUserId: Long, file: S3FileDto): ProfileImage {
        val uploadUser = memberRepository.findOneById(uploadUserId)
        val profileImage = ProfileImage.withoutMember(
            bucket = file.bucket,
            fileKey = file.fileKey,
            cloudFrontURL = file.cloudFrontURL,
            s3URL = file.s3URL,
            uploadedBy = uploadUser,
        )
        return profileImageRepository.save(profileImage)
    }
}
