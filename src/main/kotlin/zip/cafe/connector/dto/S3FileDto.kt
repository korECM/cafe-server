package zip.cafe.connector.dto

data class S3FileDto(
    val bucket: String,
    val fileKey: String,
    val s3URL: String,
    val cloudFrontURL: String
)
