package io.github.qixiaoo.crystallite.data.network

import io.github.qixiaoo.crystallite.data.model.Gender
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ComickNetwork @Inject constructor() : ComickNetworkDataSource {
    private val comickNetworkApi = RetrofitServiceCreator.create<ComickNetworkApi>()

    override suspend fun top(gender: Gender, isMature: Boolean) =
        comickNetworkApi.top(gender, isMature)

    override suspend fun comic(slug: String) = comickNetworkApi.comic(slug)

    override suspend fun chapters(hid: String, language: String?, page: Int?) =
        comickNetworkApi.chapters(hid, language, page)
}