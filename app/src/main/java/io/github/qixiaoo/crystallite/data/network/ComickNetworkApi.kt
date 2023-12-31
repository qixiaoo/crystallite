package io.github.qixiaoo.crystallite.data.network

import io.github.qixiaoo.crystallite.common.DEFAULT_CHAPTER_PAGE_SIZE
import io.github.qixiaoo.crystallite.data.model.ComicChapters
import io.github.qixiaoo.crystallite.data.model.ComicDetail
import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.model.ReadingChapter
import io.github.qixiaoo.crystallite.data.model.SearchResultAuthor
import io.github.qixiaoo.crystallite.data.model.SearchResultComic
import io.github.qixiaoo.crystallite.data.model.TopComics
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ComickNetworkApi {
    @GET("top")
    suspend fun top(
        @Query("gender") gender: Gender? = null,
        @Query("accept_mature_content") isMature: Boolean = false,
    ): TopComics

    @GET("comic/{slug}")
    suspend fun comic(@Path("slug") slug: String): ComicDetail

    @GET("comic/{hid}/chapters")
    suspend fun chapters(
        @Path("hid") hid: String,
        @Query("lang") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") pageSize: Int? = DEFAULT_CHAPTER_PAGE_SIZE,
    ): ComicChapters

    @GET("chapter/{hid}")
    suspend fun chapter(
        @Path("hid") hid: String,
        @Query("tachiyomi") tachiyomi: Boolean = true,
    ): ReadingChapter

    @GET("v1.0/search")
    suspend fun searchComicByKeyword(
        @Query("q") keyword: String,
        @Query("type") type: String = "comic",
        @Query("t") includeAltTitle: Boolean = true,
        @Query("tachiyomi") tachiyomi: Boolean = true,
    ): List<SearchResultComic>

    @GET("v1.0/search")
    suspend fun searchAuthorByKeyword(
        @Query("q") keyword: String,
        @Query("type") type: String = "author",
        @Query("t") includeAltTitle: Boolean = true,
        @Query("tachiyomi") tachiyomi: Boolean = true,
    ): List<SearchResultAuthor>
}