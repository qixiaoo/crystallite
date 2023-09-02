package io.github.qixiaoo.crystallite.ui.screens.reader

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.qixiaoo.crystallite.common.Result
import io.github.qixiaoo.crystallite.common.asResult
import io.github.qixiaoo.crystallite.data.model.ReadingChapter
import io.github.qixiaoo.crystallite.data.network.ComickRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class ReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, val comickRepository: ComickRepository
) : ViewModel() {

    private val readerArgs: ReaderArgs = ReaderArgs(savedStateHandle)

    private val chapterHid = readerArgs.chapterHid

    val chapterUiState: StateFlow<ChapterUiState> = chapterUiState(
        chapterHid = chapterHid, comickRepository = comickRepository
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ChapterUiState.Loading
    )
}

private fun chapterUiState(chapterHid: String, comickRepository: ComickRepository): Flow<ChapterUiState> {
    Log.v(::chapterUiState.name, "fetch reading chapter: $chapterHid")
    val comicSteam = comickRepository.chapter(chapterHid)
    return comicSteam.asResult().map { result ->
        when (result) {
            is Result.Error -> ChapterUiState.Error(result.exception?.message)
            is Result.Loading -> ChapterUiState.Loading
            is Result.Success -> ChapterUiState.Success(chapter = result.data)
        }
    }
}

sealed interface ChapterUiState {
    data class Success(val chapter: ReadingChapter) : ChapterUiState
    data class Error(val message: String? = null) : ChapterUiState
    object Loading : ChapterUiState
}