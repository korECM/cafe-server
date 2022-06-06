package zip.cafe.entity

import com.fasterxml.jackson.annotation.JsonCreator
import java.util.*

enum class Food(val description: String) {
    COFFEE("커피"), BEVERAGE("음료"), BAKERY("베이커리"), ETC("기타 메뉴");

    companion object {
        @JvmStatic
        @JsonCreator
        fun from(name: String) = Food.valueOf(name.uppercase(Locale.getDefault()))
    }
}
