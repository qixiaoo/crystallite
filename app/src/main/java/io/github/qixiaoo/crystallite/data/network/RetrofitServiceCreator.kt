package io.github.qixiaoo.crystallite.data.network

import android.app.Application
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private fun getHttpClient(app: Application): OkHttpClient {
    val size = 10 * 1024 * 1024 // 10 MB
    val cache = Cache(app.getCacheDir(), size.toLong())

    return OkHttpClient.Builder()
        .cache(cache)
        .build()
}

object RetrofitServiceCreator {
    private lateinit var application: Application

    private lateinit var retrofit: Retrofit

    private const val BASE_URL = "https://api.comick.fun/"

    fun initialize(app: Application) {
        application = app

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getHttpClient(app))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}