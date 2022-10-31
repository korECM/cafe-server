package zip.cafe.api

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.post
import zip.cafe.api.profile.dto.InitProfileRequest
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.restdocs.*
import zip.cafe.api.utils.spec.WebMvcTestSpec
import zip.cafe.connector.dto.S3FileDto
import zip.cafe.seeds.MOCK_MVC_USER_ID
import zip.cafe.seeds.createProfileImage
import zip.cafe.service.MemberService

@WebMvcTest(MemberController::class)
class MemberControllerTest : WebMvcTestSpec() {

    @MockkBean
    private lateinit var memberService: MemberService

    init {
        "닉네임 중복 체크" {
            val nickname = "닉네임"

            every { memberService.checkNicknameDuplication(nickname) } returns false

            val response = mockMvc.post("/members/nickname/duplicate") {
                param("nickname", nickname)
            }

            response.andExpect {
                status { isOk() }
            }.andDo {
                documentWithHandle(
                    "nickname-duplicate-check",
                    requestParameters(
                        "nickname" means "닉네임" example nickname,
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                        "isDuplicated" type BOOLEAN means "중복 여부" example false
                    )
                )
            }
        }

        "프로필 이미지 업로드" {
            val memberId = MOCK_MVC_USER_ID
            val bucket = "bucket"
            val fileName = "file1"

            val reviewImageId = 3L

            val targetFile = MockMultipartFile(fileName, "testData".toByteArray())
            val cloudFrontURL = "https://cloudfront.com"
            val uploadedFile = S3FileDto(bucket, fileName, "url", cloudFrontURL)

            val profileImage = createProfileImage(id = reviewImageId, cloudFrontURL = cloudFrontURL)

            every { memberService.uploadProfileImage(any()) } returns uploadedFile
            every { memberService.saveUploadedProfileImage(memberId, uploadedFile) } returns profileImage

            val response = mockMvc.multipart("/members/images") {
                file("image", targetFile.bytes)
            }

            response.andExpect {
                status { isCreated() }
            }
                .andDo {
                    documentWithHandle(
                        "upload-profile-image",
                        RequestDocumentation.requestParts(
                            RequestDocumentation.partWithName("image").description("업로드 할 프로필 이미지")
                        ),
                        responseBody(
                            "body" beneathPathWithSubsectionId "body",
                            "id" type NUMBER means "이미지 id" example "3",
                            "url" type STRING means "이미지 주소" example "https://techblog.woowahan.com/wp-content/uploads/img/2020-05-13/rest-docs-09.png",
                        )
                    )
                }
        }

        "프로필 초기 설정" {
            val userId = MOCK_MVC_USER_ID
            val nickname = "닉네임"
            val imageId = 5L
            val request = InitProfileRequest(nickname = nickname, imageId = imageId)

            every { memberService.initMemberProfile(userId, nickname, imageId) } just Runs

            val response = mockMvc.post("/members/profile/init") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }

            response.andExpect {
                status { isCreated() }
            }.andDo {
                documentWithHandle(
                    "init-profile",
                    requestFields(
                        "nickname" type STRING means "닉네임" example nickname,
                        "imageId" type NUMBER means "프로필 이미지 Id" example imageId,
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body",
                    )
                )
            }
        }
    }
}
