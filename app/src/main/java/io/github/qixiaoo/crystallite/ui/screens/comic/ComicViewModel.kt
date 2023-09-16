package io.github.qixiaoo.crystallite.ui.screens.comic

import android.util.Log
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
import io.github.qixiaoo.crystallite.data.repository.ComickRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class ComicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, val comickRepository: ComickRepository
) : ViewModel() {

    private val comicArgs: ComicArgs = ComicArgs(savedStateHandle)

    val slug = comicArgs.slug

    val comicUiState: StateFlow<ComicUiState> = comicUiState(
        slug = slug, comickRepository = comickRepository
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ComicUiState.Loading
    )
}


class ChaptersViewModel(
    hid: String, comickRepository: ComickRepository
) : ViewModel() {

    val chaptersUiState: Flow<PagingData<ChapterDetail>> = chaptersUiState(
        hid = hid,
        language = DEFAULT_LANGUAGE,
        scope = viewModelScope,
        comickRepository = comickRepository
    )
}


private fun comicUiState(slug: String, comickRepository: ComickRepository): Flow<ComicUiState> {
    Log.v(::comicUiState.name, "fetch comic detail: $slug")
    val comicSteam = comickRepository.comic(slug)
    return comicSteam.asResult().map { result ->
        when (result) {
            is Result.Error -> ComicUiState.Error(result.exception?.message)
            is Result.Loading -> ComicUiState.Loading
            is Result.Success -> ComicUiState.Success(comic = result.data)
        }
    }
}


private fun chaptersUiState(
    hid: String, language: String, scope: CoroutineScope, comickRepository: ComickRepository
): Flow<PagingData<ChapterDetail>> {
    Log.v(::chaptersUiState.name, "fetch comic chapters: $hid")
    return comickRepository.chapters(hid = hid, language = language).cachedIn(scope)
}


sealed interface ComicUiState {
    data class Success(val comic: ComicDetail) : ComicUiState
    data class Error(val message: String? = null) : ComicUiState
    object Loading : ComicUiState
}
