package study.cafe.api.dto

import study.cafe.entity.CafeKeyword

data class KeywordListElement(
    val id: Long,
    val keyword: String,
    val emoji: String
) {
    companion object {
        fun from(keyword: CafeKeyword): KeywordListElement {
            return KeywordListElement(
                id = keyword.id,
                keyword = keyword.keyword,
                emoji = keyword.emoji
            )
        }
    }
}
