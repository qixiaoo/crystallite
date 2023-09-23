package io.github.qixiaoo.crystallite.data.network

import io.github.qixiaoo.crystallite.data.model.ComicChapters
import io.github.qixiaoo.crystallite.data.model.ComicDetail
import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.model.ReadingChapter
import io.github.qixiaoo.crystallite.data.model.SearchResultAuthor
import io.github.qixiaoo.crystallite.data.model.SearchResultComic
import io.github.qixiaoo.crystallite.data.model.TopComics

/**
 * Interface representing network calls to the Comick backend
 */
interface ComickNetworkDataSource {
    suspend fun top(gender: Gender? = null, isMature: Boolean = false): TopComics
    suspend fun comic(slug: String): ComicDetail
    suspend fun chapters(hid: String, language: String? = null, page: Int? = null): ComicChapters
    suspend fun chapter(hid: String): ReadingChapter
    suspend fun searchComicByKeyword(keyword: String): List<SearchResultComic>
    suspend fun searchAuthorByKeyword(keyword: String): List<SearchResultAuthor>
}