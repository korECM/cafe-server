package zip.cafe.utils

import zip.cafe.entity.common.BaseTimeClass
import javax.persistence.Id
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

fun <T : BaseTimeClass, U> setEntityId(entityId: U, entity: T): T {
    val kClass = entity.javaClass.kotlin
    val idField = kClass.declaredMemberProperties.find { it.javaField?.getAnnotation(Id::class.java) != null }
        ?: throw IllegalStateException("주어진 엔티티에 @Id 필드가 존재하지 않습니다")
    idField.javaField?.trySetAccessible()
    idField.javaField?.set(entity, entityId)
    return entity
}
