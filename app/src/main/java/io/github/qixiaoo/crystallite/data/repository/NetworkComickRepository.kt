package io.github.qixiaoo.crystallite.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.github.qixiaoo.crystallite.common.DEFAULT_CHAPTER_PAGE_SIZE
import io.github.qixiaoo.crystallite.data.model.ComicChapters
import io.github.qixiaoo.crystallite.data.model.ComicDetail
import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.model.ReadingChapter
import io.github.qixiaoo.crystallite.data.model.SearchResultAuthor
import io.github.qixiaoo.crystallite.data.model.SearchResultComic
import io.github.qixiaoo.crystallite.data.model.TopComics
import io.github.qixiaoo.crystallite.data.network.ChapterPagingSource
import io.github.qixiaoo.crystallite.data.network.ComickNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class NetworkComickRepository @Inject constructor(private val network: ComickNetworkDataSource) :
    ComickRepository {

    override fun top(gender: Gender?, isMature: Boolean): Flow<TopComics> {
        return flow {
            val response = network.top(gender, isMature)
            emit(response)
        }
    }

    override fun comic(slug: String): Flow<ComicDetail> {
        return flow {
            val response = network.comic(slug)
            emit(response)
        }
    }

    override fun chapters(hid: String, language: String?, page: Int?): Flow<ComicChapters> {
        return flow {
            val response = network.chapters(hid, language, page)
            emit(response)
        }
    }

    override fun chapters(hid: String, language: String?) =
        Pager(config = PagingConfig(pageSize = DEFAULT_CHAPTER_PAGE_SIZE)) {
            ChapterPagingSource(
                hid = hid, language = language, backend = network
            )
        }.flow

    override fun chapter(hid: String): Flow<ReadingChapter> {
        return flow {
            val response = network.chapter(hid)
            emit(response)
        }
    }

    override fun searchComicByKeyword(keyword: String): Flow<List<SearchResultComic>> {
        return flow {
            val response = network.searchComicByKeyword(keyword)
            emit(response)
        }
    }

    override fun searchAuthorByKeyword(keyword: String): Flow<List<SearchResultAuthor>> {
        return flow {
            val response = network.searchAuthorByKeyword(keyword)
            emit(response)
        }
    }
}
