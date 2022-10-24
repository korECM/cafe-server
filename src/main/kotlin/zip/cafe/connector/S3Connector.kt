package zip.cafe.connector

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import zip.cafe.connector.dto.S3FileDto
import zip.cafe.util.logger
import java.io.IOException
import java.lang.System.currentTimeMillis
import java.util.UUID.randomUUID

@ConstructorBinding
@ConfigurationProperties(prefix = "cloud.aws.cloud-front")
data class S3ConnectorConfig(
    val bucketDomainMap: Map<String, String>,
)

@Component
class S3Connector(
    private val amazonS3Client: AmazonS3Client,
    private val s3ConnectorConfig: S3ConnectorConfig,
) {
    companion object {
        const val FILE_EXTENSION_SEPARATOR = "."
        const val SEPARATOR = "_"
        const val DIR_SEPARATOR = "/"
    }

    fun uploadFile(bucketName: String, dirName: String, multipartFile: MultipartFile): S3FileDto {
        if (multipartFile.isEmpty) {
            throw RuntimeException()
        }
        val fileName = buildFileName(multipartFile.originalFilename!!)
        val fileKey = dirName + DIR_SEPARATOR + fileName
        val objectMetadata = ObjectMetadata().apply {
            contentType = multipartFile.contentType
            contentLength = multipartFile.size
        }

        try {
            val inputStream = multipartFile.inputStream
            amazonS3Client.putObject(
                PutObjectRequest(bucketName, fileKey, inputStream, objectMetadata)
                    .withCannedAcl(PublicRead)
            )
            logger().info("save {} in bucket[{}]", fileKey, bucketName)
            return S3FileDto(
                bucket = bucketName,
                fileKey = fileKey,
                s3URL = createS3URL(bucketName, fileKey),
                cloudFrontURL = createCloudFrontURL(bucketName, fileKey)
            )
        } catch (e: IOException) {
            throw RuntimeException()
        }
    }

    private fun buildFileName(originalFileName: String): String {
        val fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR)
        val fileExtension = originalFileName.substring(fileExtensionIndex)
        val now = currentTimeMillis().toString()
        return randomUUID().toString() + SEPARATOR + now + fileExtension
    }

    private fun createS3URL(bucketName: String, fileKey: String): String {
        return "https://$bucketName.s3.ap-northeast-2.amazonaws.com/$fileKey"
    }

    private fun createCloudFrontURL(bucketName: String, fileKey: String): String {
        val cloudFrontURL = s3ConnectorConfig.bucketDomainMap[bucketName] ?: throw RuntimeException("S3 bucket[$bucketName]과 일치하는 Cloud Front 주소가 없습니다")
        return "$cloudFrontURL/$fileKey"
    }
}
