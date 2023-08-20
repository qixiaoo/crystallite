package io.github.qixiaoo.crystallite.data.network

import io.github.qixiaoo.crystallite.common.DEFAULT_CHAPTER_PAGE_SIZE
import io.github.qixiaoo.crystallite.data.model.ComicChapters
import io.github.qixiaoo.crystallite.data.model.ComicDetail
import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.model.TopComics
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ComickNetworkApi {
    @GET("top")
    suspend fun top(
        @Query("gender") gender: Gender,
        @Query("accept_mature_content") isMature: Boolean = false
    ): TopComics

    @GET("comic/{slug}")
    suspend fun comic(@Path("slug") slug: String): ComicDetail

    @GET("comic/{hid}/chapters")
    suspend fun chapters(
        @Path("hid") hid: String,
        @Query("lang") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") pageSize: Int? = DEFAULT_CHAPTER_PAGE_SIZE
    ): ComicChapters
}