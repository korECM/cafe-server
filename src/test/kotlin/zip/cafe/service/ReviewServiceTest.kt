@file:Suppress("UNCHECKED_CAST")

package zip.cafe.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import zip.cafe.connector.S3Connector
import zip.cafe.connector.dto.S3FileDto
import zip.cafe.entity.FloatScore
import zip.cafe.entity.Food
import zip.cafe.entity.IntScore
import zip.cafe.entity.ReviewImage
import zip.cafe.entity.member.Member
import zip.cafe.entity.review.Purpose
import zip.cafe.repository.*
import zip.cafe.seeds.createFloatScore
import zip.cafe.seeds.createIntScore
import zip.cafe.seeds.createMember
import zip.cafe.seeds.createReviewImage
import zip.cafe.service.dto.ReviewRegisterDto
import zip.cafe.utils.answersWithEntityId
import zip.cafe.utils.faker
import zip.cafe.utils.newEntityId
import java.nio.charset.StandardCharsets.UTF_8

class ReviewServiceTest : FreeSpec({

    val memberRepository: MemberRepository = mockk(relaxed = true)
    val cafeRepository: CafeRepository = mockk(relaxed = true)
    val cafeKeywordRepository: CafeKeywordRepository = mockk(relaxed = true)
    val reviewRepository: ReviewRepository = mockk(relaxed = true)
    val reviewImageRepository: ReviewImageRepository = mockk(relaxed = true)
    val footprintRepository: FootprintRepository = mockk(relaxed = true)
    val s3Connector: S3Connector = mockk(relaxed = true)
    val reviewImageBucket = "test-bucket"
    val reviewService = ReviewService(
        memberRepository = memberRepository,
        cafeRepository = cafeRepository,
        cafeKeywordRepository = cafeKeywordRepository,
        reviewRepository = reviewRepository,
        reviewImageRepository = reviewImageRepository,
        footprintRepository = footprintRepository,
        s3Connector = s3Connector,
        reviewImageBucket = reviewImageBucket,
    )

    "createReview" - {
        "사용자가 올렸던 리뷰 이미지가 아닌 경우 IllegalArgumentException 예외를 던진다" {
            // given
            val cafeId = 123L

            val uploaderMemberId = 1L
            val uploader = createMember(id = uploaderMemberId)
            val anotherUploader = createMember()

            val reviewImageIds = listOf(5L, 6L)
            val reviewImages = listOf(
                createReviewImage(id = reviewImageIds[0]),
                createReviewImage(id = reviewImageIds[1], uploadedBy = anotherUploader),
            )

            val dto = createReviewRegisterDto(
                reviewImages = reviewImageIds
            )
            // mock
            every { memberRepository.findByIdOrNull(uploaderMemberId) } returns uploader
            every { reviewImageRepository.findByIdIn(reviewImageIds) } returns reviewImages
            // when
            shouldThrow<IllegalArgumentException> {
                reviewService.createReview(
                    cafeId = cafeId,
                    uploadMemberId = uploaderMemberId,
                    dto = dto
                )
            }
            // then
        }
    }

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
            every { reviewImageRepository.save(any()) } answersWithEntityId faker.newEntityId()
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
            footprintRepository,
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


val randomFoodList = Food.values()
    get() {
        field.shuffle()
        return field
    }

fun createReviewRegisterDto(
    visitPurpose: Purpose = faker.random.nextEnum(Purpose::class.java),
    visitPurposeScore: IntScore = createIntScore(),
    foodInfoLength: Int = faker.random.nextInt(0, Food.values().size - 1),
    foodInfos: List<ReviewRegisterDto.FoodInfo> = randomFoodList.slice(0..foodInfoLength).map { ReviewRegisterDto.FoodInfo(it, createIntScore()) },
    keywordLength: Int = faker.random.nextInt(1, 5),
    keywords: List<Long> = List(keywordLength) { faker.random.nextLong(10) },
    reviewImageLength: Int = faker.random.nextInt(1, 5),
    reviewImages: List<Long> = List(reviewImageLength) { faker.random.nextLong(10) },
    finalScore: FloatScore = createFloatScore()
) = ReviewRegisterDto(
    visitPurpose = visitPurpose,
    visitPurposeScore = visitPurposeScore,
    foodInfos = foodInfos,
    keywords = keywords,
    reviewImageIds = reviewImages,
    description = "",
    finalScore = finalScore
)
