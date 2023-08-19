package io.github.qixiaoo.crystallite.data.network

import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.model.TopComics
import retrofit2.http.GET
import retrofit2.http.Query

interface ComickNetworkApi {
    @GET("top")
    suspend fun top(
        @Query("gender") gender: Gender,
        @Query("accept_mature_content") isMature: Boolean = false
    ): TopComics
}