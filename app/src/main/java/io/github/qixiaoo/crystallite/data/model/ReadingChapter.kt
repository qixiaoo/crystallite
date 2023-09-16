package io.github.qixiaoo.crystallite.data.model

import com.google.gson.annotations.SerializedName

data class ReadingChapter(
    var chapter: ReadingChapterDetail,
    val next: ChapterToBeRead?,
    val prev: ChapterToBeRead?,
)

data class ReadingChapterDetail(
    val id: String,
    val hid: String,
    val lang: String,
    val title: String?,
    val images: List<ChapterImage>,
    @SerializedName("chap") val chapter: String?,
    @SerializedName("vol") val volume: String?,
    @SerializedName("group_name") val groupNameList: List<String>?,
    @SerializedName("md_comics") val mdComic: MdComic?,
)

data class ChapterImage(val w: Int, val h: Int, val url: String)

data class ChapterToBeRead(
    val id: String,
    val hid: String,
    val lang: String,
    val title: String?,
    @SerializedName("vol") val volume: String?,
    @SerializedName("chap") val chapter: String,
)

data class MdComic(
    val id: Int,
    val slug: String,
    val title: String,
)