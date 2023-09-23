package io.github.qixiaoo.crystallite.data.network

import io.github.qixiaoo.crystallite.data.model.Gender
import javax.inject.Inject


class ComickNetwork @Inject constructor() : ComickNetworkDataSource {
    private val comickNetworkApi = RetrofitServiceCreator.create<ComickNetworkApi>()

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