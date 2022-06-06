package zip.cafe.entity.review

import com.fasterxml.jackson.annotation.JsonCreator
import java.util.Locale.getDefault

enum class Purpose(val description: String) {
    STUDY("공부"), TALK("대화"), DATE("데이트"), ETC("기타");

    companion object {
        @JvmStatic
        @JsonCreator
        fun from(name: String) = Purpose.valueOf(name.uppercase(getDefault()))
    }
}
