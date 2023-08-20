package io.github.qixiaoo.crystallite.data.network

import io.github.qixiaoo.crystallite.data.model.ComicChapters
import io.github.qixiaoo.crystallite.data.model.ComicDetail
import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.model.TopComics

/**
 * Interface representing network calls to the Comick backend
 */
interface ComickNetworkDataSource {
    suspend fun top(gender: Gender, isMature: Boolean): TopComics
    suspend fun comic(slug: String): ComicDetail
    suspend fun chapters(hid: String, language: String? = null, page: Int? = null): ComicChapters
}