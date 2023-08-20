package io.github.qixiaoo.crystallite.data.network

import androidx.paging.PagingData
import io.github.qixiaoo.crystallite.data.model.ChapterDetail
import io.github.qixiaoo.crystallite.data.model.ComicChapters
import io.github.qixiaoo.crystallite.data.model.ComicDetail
import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.model.TopComics
import kotlinx.coroutines.flow.Flow

interface ComickRepository {
    fun top(gender: Gender, isMature: Boolean = false): Flow<TopComics>
    fun comic(slug: String): Flow<ComicDetail>
    fun chapters(hid: String, language: String? = null, page: Int? = null): Flow<ComicChapters>
    fun chapters(hid: String, language: String? = null): Flow<PagingData<ChapterDetail>>
}