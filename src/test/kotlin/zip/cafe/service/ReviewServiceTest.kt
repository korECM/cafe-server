@file:Suppress("UNCHECKED_CAST")

package zip.cafe.service

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import zip.cafe.connector.S3Connector
import zip.cafe.connector.dto.S3FileDto
import zip.cafe.entity.ReviewImage
import zip.cafe.entity.member.Member
import zip.cafe.repository.*
import zip.cafe.seeds.createMember
import zip.cafe.utils.answersWithEntityId
import zip.cafe.utils.faker
import java.nio.charset.StandardCharsets.UTF_8

class ReviewServiceTest : FreeSpec({

    val memberRepository: MemberRepository = mockk(relaxed = true)
    val cafeRepository: CafeRepository = mockk(relaxed = true)
    val cafeKeywordRepository: CafeKeywordRepository = mockk(relaxed = true)
    val reviewRepository: ReviewRepository = mockk(relaxed = true)
    val reviewImageRepository: ReviewImageRepository = mockk(relaxed = true)
    val s3Connector: S3Connector = mockk(relaxed = true)
    val reviewImageBucket = "test-bucket"
    val reviewService = ReviewService(
        memberRepository = memberRepository,
        cafeRepository = cafeRepository,
        cafeKeywordRepository = cafeKeywordRepository,
        reviewRepository = reviewRepository,
        reviewImageRepository = reviewImageRepository,
        s3Connector = s3Connector,
        reviewImageBucket = reviewImageBucket,
    )

    "uploadReviewImages" - {
        "이미지를 S3에 올리고 그 정보를 반환한다" {
            // given
            val file = createMultipartFile()
            val s3FileDto = createS3FileDto(reviewImageBucket)
            // mock
            every { s3Connector.uploadFile(bucketName = reviewImageBucket, dirName = "org", multipartFile = file) } returns s3FileDto
            // when
            val uploadReviewImages = reviewService.uploadReviewImages(listOf(file))
            // then
            uploadReviewImages shouldContainExactly listOf(s3FileDto)
        }
    }

    "saveUploadedReviewImage" - {
        "s3에 올라간 이미지를 DB에 저장한다" {
            // given
            val uploaderUserId = 29L
            val uploader = createMember(id = uploaderUserId)

            val s3FileDtos = listOf(createS3FileDto(reviewImageBucket))
            // mock
            every { memberRepository.findByIdOrNull(uploaderUserId) } returns uploader
            every { reviewImageRepository.save(any()) } answersWithEntityId faker.random.nextLong()
            // when
            val reviewImages = reviewService.saveUploadedReviewImage(uploaderUserId, s3FileDtos)
            // then
            reviewImages.forEachIndexed { index, reviewImage ->
                compareReviewImageAndS3File(reviewImage, reviewImageBucket, s3FileDtos[index], uploader)
            }
            s3FileDtos.forEach { s3File ->
                verify {
                    reviewImageRepository.save(
                        match {
                            compareReviewImageAndS3File(it, reviewImageBucket, s3File, uploader)
                        }
                    )
                }
            }

            confirmVerified(reviewImageRepository)
        }
    }

    afterTest {
        clearMocks(
            memberRepository,
            cafeRepository,
            cafeKeywordRepository,
            reviewRepository,
            reviewImageRepository,
            s3Connector,
        )
    }
})

fun createMultipartFile(name: String = faker.animal.name(), testData: String = faker.quote.fortuneCookie()): MockMultipartFile {
    val fileContent: ByteArray = testData.toByteArray(UTF_8)
    return MockMultipartFile("$name.png", fileContent)
}

fun createS3FileDto(bucket: String) =
    S3FileDto(bucket = bucket, fileKey = faker.name.name(), s3URL = faker.internet.domain(), cloudFrontURL = faker.internet.domain())

private fun compareReviewImageAndS3File(
    reviewImage: ReviewImage,
    reviewImageBucket: String,
    s3File: S3FileDto,
    uploader: Member
) = reviewImage.review == null &&
    reviewImage.id != 0L &&
    reviewImage.bucket == reviewImageBucket &&
    reviewImage.cloudFrontURL == s3File.cloudFrontURL &&
    reviewImage.fileKey == s3File.fileKey &&
    reviewImage.s3URL == s3File.s3URL &&
    reviewImage.uploadedBy == uploader
