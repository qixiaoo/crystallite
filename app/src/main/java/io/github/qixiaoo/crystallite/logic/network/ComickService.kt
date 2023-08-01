package io.github.qixiaoo.crystallite.logic.network

import io.github.qixiaoo.crystallite.logic.model.Gender
import io.github.qixiaoo.crystallite.logic.model.TopComics
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ComickService {
    @GET("top")
    fun top(
        @Query("gender") gender: Gender,
        @Query("accept_mature_content") isMature: Boolean = false
    ): Call<TopComics>
}