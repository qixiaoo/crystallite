package io.github.qixiaoo.crystallite.data.model

import com.google.gson.annotations.SerializedName


data class TopComics(
    val rank: List<Comic>, // Popular Ongoing
    val recentRank: List<Comic>,
    val trending: ComicListOrderedByPeriod, // Most Viewed
    val news: List<Comic>, // Recently Added
    val extendedNews: List<Comic>,
    val completions: List<Comic>, // Complete Series
    val topFollowNewComics: ComicListOrderedByPeriod, // Popular New Comics
    val topFollowComics: ComicListOrderedByPeriod,
)

data class ComicListOrderedByPeriod(
    @SerializedName("7") val week: List<Comic>,
    @SerializedName("30") val month: List<Comic>,
    @SerializedName("90") val quarter: List<Comic>,
    @SerializedName("180") val halfYear: List<Comic>?,
    @SerializedName("270") val threeQuarters: List<Comic>?,
    @SerializedName("360") val year: List<Comic>?,
    @SerializedName("720") val twoYears: List<Comic>?,
)