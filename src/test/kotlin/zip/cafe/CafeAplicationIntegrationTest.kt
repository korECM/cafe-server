package zip.cafe

import io.kotest.core.annotation.Tags
import io.kotest.core.spec.style.FreeSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest

@Tags(integrationTestTag)
@SpringBootTest
class CafeAplicationIntegrationTest : FreeSpec({
    "init Test" {
    }
}) {
    override fun extensions() = listOf(SpringExtension)
}
