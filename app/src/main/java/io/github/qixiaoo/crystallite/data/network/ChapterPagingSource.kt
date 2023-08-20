package io.github.qixiaoo.crystallite.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.github.qixiaoo.crystallite.data.model.ChapterDetail


class ChapterPagingSource(
    val hid: String, val language: String? = null, val backend: ComickNetworkDataSource
) : PagingSource<Int, ChapterDetail>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, ChapterDetail> {
        return try {
            val pageNumber = params.key ?: 1
            val response = backend.chapters(hid = hid, language = language, page = pageNumber)

            val (chapters, total, limit) = response
            val hasNextPage = total > limit * pageNumber
            val nextPageNumber = if (hasNextPage) pageNumber + 1 else null
            val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null

            LoadResult.Page(
                data = chapters, prevKey = prevPageNumber, nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ChapterDetail>): Int? {
        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
