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

    val chapterUiState = MutableStateFlow<ChapterUiState>(ChapterUiState.Loading)

    var current = MutableStateFlow(0)

    val chapter: (ReadingChapter?)
        get() {
            return (chapterUiState.value as? ChapterUiState.Success)?.chapter
        }

    val imageList: List<String>
        get() {
            return chapter?.chapter?.images?.map { it.url } ?: emptyList()
        }

    val isPrevChapterEnabled: Boolean
        get() {
            return chapter?.prev?.hid?.isNotEmpty() != null
        }

    val isNextChapterEnabled: Boolean
        get() {
            return chapter?.next?.hid?.isNotEmpty() != null
        }

    fun setCurrent(value: Int) {
        current.value = value
    }

    fun navigateToPrevChapter() {
        viewModelScope.launch {
            if (chapter == null) {
                return@launch
            }
            val readingChapter = chapter
            if (readingChapter?.prev?.hid?.isNotEmpty() == true) {
                chapterUiState.emit(ChapterUiState.Loading)
                chapterHid.emit(readingChapter.prev.hid)
                setCurrent(0)
            }
        }
    }

    fun navigateToNextChapter() {
        viewModelScope.launch {
            if (chapter == null) {
                return@launch
            }
            val readingChapter = chapter
            if (readingChapter?.next?.hid?.isNotEmpty() == true) {
                chapterUiState.emit(ChapterUiState.Loading)
                chapterHid.emit(readingChapter.next.hid)
                setCurrent(0)
            }
        }
    }

    init {
        viewModelScope.launch {
            chapterUiState(chapterHid = chapterHid, comickRepository = comickRepository).collect {
                chapterUiState.emit(it)
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun chapterUiState(
    chapterHid: MutableStateFlow<String>,
    comickRepository: ComickRepository,
): Flow<ChapterUiState> {
    val chapterSteam = chapterHid.flatMapLatest {
        Log.v(::chapterUiState.name, "fetch reading chapter: $it")
        comickRepository.chapter(it)
    }
    return chapterSteam.asResult().map { result ->
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