package zip.cafe.utils

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import zip.cafe.entity.common.BaseClass
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class TestEntity(
    val someProperty: String
) : BaseClass() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id", nullable = false)
    val id: Long = 0
}

class EntityIdHelperTest : FreeSpec({

    "전달된 엔티티의 id를 설정한다" {
        val id = faker.newEntityId()
        val entity = setEntityId(id, TestEntity("someProperty"))
        entity.id shouldBe id
    }
})
