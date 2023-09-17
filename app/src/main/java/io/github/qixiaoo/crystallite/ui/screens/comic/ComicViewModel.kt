package io.github.qixiaoo.crystallite.ui.screens.comic

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.qixiaoo.crystallite.common.DEFAULT_LANGUAGE
import io.github.qixiaoo.crystallite.common.Result
import io.github.qixiaoo.crystallite.common.asResult
import io.github.qixiaoo.crystallite.data.model.ChapterDetail
import io.github.qixiaoo.crystallite.data.model.ComicDetail
import io.github.qixiaoo.crystallite.data.model.FollowedComic
import io.github.qixiaoo.crystallite.data.repository.ComickRepository
import io.github.qixiaoo.crystallite.data.repository.FollowedComicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import javax.inject.Inject


@HiltViewModel
class ComicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    comickRepository: ComickRepository,
    followedComicRepository: FollowedComicRepository,
) : ViewModel() {

    private val comicArgs: ComicArgs = ComicArgs(savedStateHandle)

    val slug = comicArgs.slug

    val comicUiState: StateFlow<ComicUiState> = comicUiState(
        slug = slug,
        comickRepository = comickRepository,
        followedComicRepository = followedComicRepository,
        scope = viewModelScope
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ComicUiState.Loading
    )
}


private fun comicUiState(
    slug: String,
    language: String = DEFAULT_LANGUAGE,
    comickRepository: ComickRepository,
    followedComicRepository: FollowedComicRepository,
    scope: CoroutineScope,
): Flow<ComicUiState> {
    Log.v(::comicUiState.name, "fetch comic detail: $slug")

    val comicSteam = comickRepository.comic(slug)
    val followedComicsStream = followedComicRepository.getFollowedComics()

    return comicSteam.zip(followedComicsStream, ::Pair).asResult().map { result ->
        when (result) {
            is Result.Error -> ComicUiState.Error(result.exception?.message)
            is Result.Loading -> ComicUiState.Loading

            is Result.Success -> {
                val (comic, followedComics) = result.data

                val hid = comic.comic.hid
                val pagingChapters =
                    comickRepository.chapters(hid = hid, language = language).cachedIn(scope)

                ComicUiState.Success(
                    comic = comic,
                    pagingChapters = pagingChapters,
                    followedComic = followedComics.map { it.hid }.contains(comic.comic.hid),
                    followedComicRepository = followedComicRepository,
                )
            }
        }
    }
}


sealed interface ComicUiState {
    object Loading : ComicUiState

    data class Error(val message: String? = null) : ComicUiState

    data class Success(
        val comic: ComicDetail,
        val pagingChapters: Flow<PagingData<ChapterDetail>>,
        private val followedComic: Boolean,
        private val followedComicRepository: FollowedComicRepository,
    ) : ComicUiState {

        val followed = mutableStateOf(followedComic)

        suspend fun toggleFollowedComic(isFollowed: Boolean, comic: FollowedComic) {
            if (isFollowed) {
                followedComicRepository.followComics(comics = listOf(comic))
            } else {
                followedComicRepository.unfollowComics(comicHids = listOf(comic.hid))
            }
            followed.value = isFollowed
        }
    }
}
