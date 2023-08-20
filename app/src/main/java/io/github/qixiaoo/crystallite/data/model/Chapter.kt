package io.github.qixiaoo.crystallite.data.model

import com.google.gson.annotations.SerializedName

data class Chapter(
    val hid: String,
    val lang: String,
    @SerializedName("chap") val chapter: String,
    @SerializedName("vol") val volume: String?,
    @SerializedName("group_name") val groupNameList: List<String>
)

data class ChapterDetail(
    val id: String,
    val hid: String,
    val lang: String,
    val title: String?,
    @SerializedName("chap") val chapter: String?,
    @SerializedName("vol") val volume: String?,
    @SerializedName("group_name") val groupNameList: List<String>?
)

data class ComicChapters(
    val chapters: List<ChapterDetail>, val total: Int, val limit: Int
)