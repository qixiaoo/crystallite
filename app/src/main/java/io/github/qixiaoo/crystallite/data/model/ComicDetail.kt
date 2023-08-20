package io.github.qixiaoo.crystallite.data.model

import com.google.gson.annotations.SerializedName

data class ComicDetail(
    val firstChap: Chapter,
    val comic: ComicDetailInfo,
    val artists: List<SlugNamePair>,
    val authors: List<SlugNamePair>,
    val langList: List<String>,
    val genres: List<SlugNamePair>,
    val matureContent: Boolean,
)

data class ComicDetailInfo(
    val id: Int,
    val hid: String,
    val slug: String,
    val title: String,
    val genres: List<Int>,
    val country: String,
    val status: ProgressStatus,
    @SerializedName("desc") val description: String?,
    @SerializedName("content_rating") val contentRating: ContentRating,
    @SerializedName("last_chapter") val lastChapter: Float?,
    @SerializedName("chapter_count") val chapterCount: Int,
    @SerializedName("md_covers") val mdCovers: List<MdCover>,
    @SerializedName("user_follow_count") val followerCount: Int,
)

data class SlugNamePair(
    val slug: String, val name: String
)

enum class ProgressStatus(val value: Int) {
    @SerializedName("1")
    ONGOING(1),

    @SerializedName("2")
    COMPLETED(2),

    @SerializedName("3")
    CANCELLED(3),

    @SerializedName("4")
    HIATUS(4),
}