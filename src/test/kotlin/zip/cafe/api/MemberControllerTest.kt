package zip.cafe.api

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.post
import zip.cafe.api.profile.dto.EditProfileRequest
import zip.cafe.api.profile.dto.InitProfileRequest
import zip.cafe.api.utils.mockmvc.documentWithHandle
import zip.cafe.api.utils.restdocs.*
import zip.cafe.connector.dto.S3FileDto
import zip.cafe.seeds.MOCK_MVC_USER_ID
import zip.cafe.seeds.createProfileImage

class MemberControllerTest : WebMvcTestAdapter() {

    init {
        "닉네임 중복 체크" {
            val memberId = MOCK_MVC_USER_ID
            val nickname = "닉네임"

            every { memberService.checkNicknameDuplication(memberId, nickname) } returns false

            val response = mockMvc.post("/members/nickname/duplicate") {
                param("nickname", nickname)
            }

            response.andExpect {
                status { isOk() }
            }.andDo {
                documentWithHandle(
                    "nickname-duplicate-check",
                    requestParameters(
                        "nickname" means "닉네임" example nickname
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
                            "url" type STRING means "이미지 주소" example "https://techblog.woowahan.com/wp-content/uploads/img/2020-05-13/rest-docs-09.png"
                        )
                    )
                }
        }

        "프로필 초기 설정" {
            val userId = MOCK_MVC_USER_ID
            val nickname = "닉네임"
            val description = "적당한 계정 소개"
            val imageId = 5L
            val request = InitProfileRequest(nickname = nickname, description = description, imageId = imageId)

            every { memberService.initMemberProfile(userId, nickname, description, imageId) } just Runs

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
                        "description" type STRING means "한 줄 소개" example description,
                        "imageId" type NUMBER means "프로필 이미지 Id" example imageId and optional
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body"
                    )
                )
            }
        }

        "프로필 수정" {
            val userId = MOCK_MVC_USER_ID
            val nickname = "닉네임"
            val description = "적당한 계정 소개"
            val imageId = 5L
            val request = EditProfileRequest(nickname = nickname, description = description, imageId = imageId)

            every { memberService.editMemberProfile(userId, nickname, description, imageId) } just Runs

            val response = mockMvc.post("/members/profile/edit") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }

            response.andExpect {
                status { isCreated() }
            }.andDo {
                documentWithHandle(
                    "edit-profile",
                    requestFields(
                        "nickname" type STRING means "닉네임" example nickname,
                        "description" type STRING means "한 줄 소개" example description,
                        "imageId" type NUMBER means "프로필 이미지 Id" example imageId and optional
                    ),
                    responseBody(
                        "body" beneathPathWithSubsectionId "body"
                    )
                )
            }
        }

        "회원 탈퇴" {
            val userId = MOCK_MVC_USER_ID

            every { memberService.deleteMember(userId) } just Runs

            val response = mockMvc.post("/members/delete") {
                contentType = MediaType.APPLICATION_JSON
            }

            response.andExpect {
                status { isAccepted() }
            }.andDo {
                documentWithHandle(
                    "delete-member",
                    responseBody(
                        "body" beneathPathWithSubsectionId "body"
                    )
                )
            }
        }
    }
}
