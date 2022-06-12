@file:Suppress("UNCHECKED_CAST")

package zip.cafe.utils

import io.mockk.MockKStubScope
import zip.cafe.entity.common.BaseTimeClass

infix fun <T : BaseTimeClass, K> MockKStubScope<T, T>.answersWithEntityId(entityId: K) {
    this.answers {
        val savedEntity = it.invocation.args[0] as T
        setEntityId(entityId, savedEntity)
    }
}
