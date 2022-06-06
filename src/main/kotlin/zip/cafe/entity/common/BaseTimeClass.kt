package zip.cafe.entity.common

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTimeClass {
    @Column(updatable = false)
    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.MIN
        private set

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.MIN
        private set
}
