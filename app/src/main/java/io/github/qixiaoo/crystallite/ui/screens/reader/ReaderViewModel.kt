package io.github.qixiaoo.crystallite.ui.screens.reader

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.qixiaoo.crystallite.common.Result
import io.github.qixiaoo.crystallite.common.asResult
import io.github.qixiaoo.crystallite.data.model.ReadingChapter
import io.github.qixiaoo.crystallite.data.network.ComickRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, comickRepository: ComickRepository,
) : ViewModel() {

    private val readerArgs: ReaderArgs = ReaderArgs(savedStateHandle)

    private val chapterHid = MutableStateFlow(readerArgs.chapterHid)

    val readerUiState = MutableStateFlow<ReaderUiState>(ReaderUiState.Loading)

    init {
        viewModelScope.launch {
            chapterUiState(chapterHid = chapterHid, comickRepository = comickRepository).collect {
                readerUiState.emit(it)
            }
        }
    }

    fun navigateToPrevChapter() {
        viewModelScope.launch {
            if (readerUiState.value !is ReaderUiState.Success) {
                return@launch
            }
            val uiState = (readerUiState.value as ReaderUiState.Success)
            if (uiState.isPrevChapterEnabled) {
                readerUiState.emit(ReaderUiState.Loading)
                uiState.readingChapter.prev?.let { chapterHid.emit(it.hid) }
            }
        }
    }

    fun navigateToNextChapter() {
        viewModelScope.launch {
            if (readerUiState.value !is ReaderUiState.Success) {
                return@launch
            }
            val uiState = (readerUiState.value as ReaderUiState.Success)
            if (uiState.isNextChapterEnabled) {
                readerUiState.emit(ReaderUiState.Loading)
                uiState.readingChapter.next?.let { chapterHid.emit(it.hid) }
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun chapterUiState(
    chapterHid: MutableStateFlow<String>,
    comickRepository: ComickRepository,
): Flow<ReaderUiState> {
    val chapterSteam = chapterHid.flatMapLatest {
        Log.v(::chapterUiState.name, "fetch reading chapter: $it")
        comickRepository.chapter(it)
    }
    return chapterSteam.asResult().map { result ->
        when (result) {
            is Result.Error -> ReaderUiState.Error(result.exception?.message)
            is Result.Loading -> ReaderUiState.Loading
            is Result.Success -> ReaderUiState.Success(readingChapter = result.data)
        }
    }
}

sealed interface ReaderUiState {
    object Loading : ReaderUiState

    data class Error(val message: String? = null) : ReaderUiState

    data class Success(val readingChapter: ReadingChapter) : ReaderUiState {
        var currentPage = MutableStateFlow(0)

        var isHudVisible = mutableStateOf(false)

        val imageList: List<String>
            get() {
                return readingChapter.chapter.images.map { it.url }
            }

        val isPrevChapterEnabled: Boolean
            get() {
                return readingChapter.prev?.hid?.isNotEmpty() != null
            }

        val isNextChapterEnabled: Boolean
            get() {
                return readingChapter.next?.hid?.isNotEmpty() != null
            }

        fun setCurrentPage(value: Int) {
            currentPage.value = value
        }
    }
}