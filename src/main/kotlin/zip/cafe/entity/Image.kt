package zip.cafe.entity

import zip.cafe.entity.common.BaseClass
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class Image(
    @Column(name = "bucket", nullable = false)
    val bucket: String,
    @Column(name = "fileKey", nullable = false)
    val fileKey: String,
    @Column(name = "s3URL", nullable = false)
    val s3URL: String,
    @Column(name = "cloudFrontURL", nullable = false)
    val cloudFrontURL: String
) : BaseClass() {
}
