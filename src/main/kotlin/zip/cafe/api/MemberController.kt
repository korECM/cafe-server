package zip.cafe.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import zip.cafe.api.dto.ApiResponse
import zip.cafe.api.dto.ApiResponse.Companion.success
import zip.cafe.api.dto.CheckNicknameDuplicationResponse
import zip.cafe.api.profile.dto.UploadedProfileImageResponse
import zip.cafe.security.LoginUserId
import zip.cafe.service.MemberService

@RequestMapping("/members")
@RestController
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping("/nickname/duplicate")
    fun nicknameDuplicationCheck(
        @RequestParam nickname: String
    ): ApiResponse<CheckNicknameDuplicationResponse> {
        val isDuplicated = memberService.checkNicknameDuplication(nickname)
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
}
