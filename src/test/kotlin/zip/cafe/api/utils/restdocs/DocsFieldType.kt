package zip.cafe.api.utils.restdocs

import org.springframework.restdocs.payload.JsonFieldType
import kotlin.reflect.KClass

sealed class DocsFieldType(
    val type: JsonFieldType
)

object ARRAY : DocsFieldType(JsonFieldType.ARRAY)
object STRING : DocsFieldType(JsonFieldType.STRING)
object DATE : DocsFieldType(JsonFieldType.STRING)
object DATETIME : DocsFieldType(JsonFieldType.STRING)
object NULL : DocsFieldType(JsonFieldType.NULL)
object NUMBER : DocsFieldType(JsonFieldType.NUMBER)
object BOOLEAN : DocsFieldType(JsonFieldType.BOOLEAN)
object OBJECT : DocsFieldType(JsonFieldType.OBJECT)

object VARIES : DocsFieldType(JsonFieldType.VARIES)

data class ENUM<T : Enum<T>>(val enums: Collection<T>) : DocsFieldType(JsonFieldType.STRING) {
    constructor(clazz: KClass<T>) : this(clazz.java.enumConstants.asList()) // (1)
}
