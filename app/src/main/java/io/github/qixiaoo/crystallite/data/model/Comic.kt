package io.github.qixiaoo.crystallite.data.model

import com.google.gson.annotations.SerializedName

data class Comic(
    val id: Int?,
    val slug: String,
    val title: String,
    val genres: List<Int>,
    @SerializedName("demographic") val demoGraphic: Int?,
    @SerializedName("content_rating") val contentRating: ContentRating,
    @SerializedName("last_chapter") val lastChapter: Float?,
    @SerializedName("md_covers") val mdCovers: List<MdCover>,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("uploaded_at") val uploadedAt: String?,
    @SerializedName("count") val followerCount: String?,
)

data class MdCover(val w: Int, val h: Int, val b2key: String)

enum class ContentRating {
    @SerializedName("suggestive")
    SUGGESTIVE,

    @SerializedName("safe")
    SAFE,

    @SerializedName("erotica")
    EROTICA,
}