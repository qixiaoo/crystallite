package io.github.qixiaoo.crystallite.data.network

import io.github.qixiaoo.crystallite.data.model.Gender
import retrofit2.Retrofit
import javax.inject.Inject


class ComickNetwork @Inject constructor(private val retrofit: Retrofit) : ComickNetworkDataSource {
    private val comickNetworkApi = retrofit.create(ComickNetworkApi::class.java)

    override suspend fun top(gender: Gender?, isMature: Boolean) =
        comickNetworkApi.top(gender, isMature)

    override suspend fun comic(slug: String) = comickNetworkApi.comic(slug)

    override suspend fun chapters(hid: String, language: String?, page: Int?) =
        comickNetworkApi.chapters(hid, language, page)

    override suspend fun chapter(hid: String) = comickNetworkApi.chapter(hid)

    override suspend fun searchComicByKeyword(keyword: String) =
        comickNetworkApi.searchComicByKeyword(keyword = keyword)

    override suspend fun searchAuthorByKeyword(keyword: String) =
        comickNetworkApi.searchAuthorByKeyword(keyword = keyword)
}