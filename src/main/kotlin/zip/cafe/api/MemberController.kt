package zip.cafe.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.CheckNicknameDuplicationResponse
import zip.cafe.api.profile.dto.*
import zip.cafe.security.LoginUserId
import zip.cafe.service.MemberService

@RequestMapping("/members")
@RestController
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping("/nickname/duplicate")
    fun nicknameDuplicationCheck(
        @LoginUserId userId: Long,
        @RequestParam nickname: String,
    ): ApiResponse<CheckNicknameDuplicationResponse> {
        val isDuplicated = memberService.checkNicknameDuplication(userId, nickname)
        val response = CheckNicknameDuplicationResponse(isDuplicated)
        return success(response)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/images")
    fun uploadImage(
        @LoginUserId userId: Long,
        @RequestPart("image") image: MultipartFile,
    ): ApiResponse<UploadedProfileImageResponse> {
        val savedProfileImage = memberService.run {
            val uploadReviewImages = uploadProfileImage(image)
            saveUploadedProfileImage(userId, uploadReviewImages)
        }
        return success(UploadedProfileImageResponse.from(savedProfileImage))
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/profile/init")
    fun initProfile(
        @LoginUserId userId: Long,
        @RequestBody request: InitProfileRequest,
    ): ApiResponse<InitProfileResponse> {
        memberService.initMemberProfile(userId, request.nickname, request.description, request.imageId)
        return success(InitProfileResponse())
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/profile/edit")
    fun editProfile(
        @LoginUserId userId: Long,
        @RequestBody request: EditProfileRequest,
    ): ApiResponse<EditProfileResponse> {
        memberService.editMemberProfile(userId, request.nickname, request.description, request.imageId)
        return success(EditProfileResponse())
    }
}
