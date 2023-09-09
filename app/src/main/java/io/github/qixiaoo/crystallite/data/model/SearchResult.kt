package io.github.qixiaoo.crystallite.data.model

import com.google.gson.annotations.SerializedName

data class SearchResultComic(
    val id: Int,
    val hid: String,
    val slug: String,
    val title: String,
    val genres: List<Int>,
    val status: ProgressStatus?,
    val rating: String?,
    @SerializedName("desc") val description: String?,
    @SerializedName("content_rating") val contentRating: ContentRating,
    @SerializedName("last_chapter") val lastChapter: Float,
    @SerializedName("md_covers") val mdCovers: List<MdCover>,
)

data class SearchResultAuthor(
    val slug: String,
    val title: String,
    @SerializedName("sml") val similarity: Float,
)

enum class SearchResultType {
    @SerializedName("user")
    USER,

    @SerializedName("author")
    AUTHOR,

    @SerializedName("group")
    GROUP,

    @SerializedName("comic")
    COMIC,
}