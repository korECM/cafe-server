package zip.cafe.entity.cafe

import io.kotest.core.spec.style.FreeSpec
import nl.jqno.equalsverifier.EqualsVerifier
import zip.cafe.entity.menu.Menu
import zip.cafe.seeds.createCafe
import zip.cafe.seeds.createMenu

class CafeMenuTest : FreeSpec({
    "equalsAndHashCode" {
        EqualsVerifier.forClass(CafeMenu::class.java)
            .usingGetClass()
            .withOnlyTheseFields("cafe", "menu")
            .withPrefabValues(Cafe::class.java, createCafe(), createCafe())
            .withPrefabValues(Menu::class.java, createMenu(), createMenu())
            .verify()
    }
})
