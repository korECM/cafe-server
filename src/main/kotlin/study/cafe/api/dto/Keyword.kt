package study.cafe.api.dto

import study.cafe.entity.CafeKeyword

data class KeywordListElement(
    val keyword: String,
    val emoji: String
) {
    companion object {
        fun from(keyword: CafeKeyword): KeywordListElement {
            return KeywordListElement(
                keyword = keyword.keyword,
                emoji = keyword.emoji
            )
        }
    }
}
