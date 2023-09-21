package io.github.qixiaoo.crystallite.ui.screens.reader

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.qixiaoo.crystallite.common.Result
import io.github.qixiaoo.crystallite.common.asResult
import io.github.qixiaoo.crystallite.data.model.FollowedComic
import io.github.qixiaoo.crystallite.data.model.FollowedComicReadingChapter
import io.github.qixiaoo.crystallite.data.model.ReadingChapter
import io.github.qixiaoo.crystallite.data.model.ReadingMode
import io.github.qixiaoo.crystallite.data.repository.ComickRepository
import io.github.qixiaoo.crystallite.data.repository.FollowedComicRepository
import io.github.qixiaoo.crystallite.data.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    comickRepository: ComickRepository,
    userPreferencesRepository: UserPreferencesRepository,
    followedComicRepository: FollowedComicRepository,
) : ViewModel() {

    private val readerArgs: ReaderArgs = ReaderArgs(savedStateHandle)

    private val chapterHid = MutableStateFlow(readerArgs.chapterHid)

    private var chapterFetchedFirstTime = MutableStateFlow(false)

    val readerUiState = MutableStateFlow<ReaderUiState>(ReaderUiState.Loading)

    suspend fun navigateToPrevChapter() {
        if (readerUiState.value !is ReaderUiState.Success) {
            return
        }
        val uiState = (readerUiState.value as ReaderUiState.Success)
        if (uiState.isPrevChapterEnabled) {
            readerUiState.emit(ReaderUiState.Loading)
            uiState.readingChapter.prev?.let { chapterHid.emit(it.hid) }
        }
    }

    suspend fun navigateToNextChapter() {
        if (readerUiState.value !is ReaderUiState.Success) {
            return
        }
        val uiState = (readerUiState.value as ReaderUiState.Success)
        if (uiState.isNextChapterEnabled) {
            readerUiState.emit(ReaderUiState.Loading)
            uiState.readingChapter.next?.let { chapterHid.emit(it.hid) }
        }
    }

    init {
        viewModelScope.launch {
            readerUiState(
                chapterHid = chapterHid,
                comickRepository = comickRepository,
                userPreferencesRepository = userPreferencesRepository,
                followedComicRepository = followedComicRepository,
                viewModelScope = viewModelScope
            ).collect { currentState ->
                readerUiState.emit(currentState)
            }
        }

        viewModelScope.launch {
            updateReadingProgressWhenChapterChanges(
                readerUiState = readerUiState,
                chapterFetchedFirstTime = chapterFetchedFirstTime,
                followedComicRepository = followedComicRepository
            )
        }
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
private fun readerUiState(
    chapterHid: MutableStateFlow<String>,
    comickRepository: ComickRepository,
    userPreferencesRepository: UserPreferencesRepository,
    followedComicRepository: FollowedComicRepository,
    viewModelScope: CoroutineScope,
): Flow<ReaderUiState> {
    val chapterSteam = chapterHid.flatMapLatest {
        Log.v(::readerUiState.name, "fetch reading chapter: $it")
        comickRepository.chapter(it)
    }

    val chapterWithFollowedComicStream = chapterSteam.flatMapLatest {
        val readingChapter = flowOf(it)
        val followedComic = it.chapter.mdComic?.let { comic ->
            Log.v(::readerUiState.name, "query followed comic ${comic.title} from database")
            followedComicRepository.getFollowedComic(comic.hid).take(1)
        } ?: flowOf(null)
        readingChapter.zip(followedComic, ::Pair)
    }

    return chapterWithFollowedComicStream.asResult().map { result ->
        when (result) {
            is Result.Error -> ReaderUiState.Error(result.exception?.message)
            is Result.Loading -> ReaderUiState.Loading
            is Result.Success -> {
                val (readingChapter, followedComic) = result.data

                ReaderUiState.Success(
                    readingChapter = readingChapter,
                    followedComic = followedComic,
                    viewModelScope = viewModelScope,
                    userPreferencesRepository = userPreferencesRepository,
                    followedComicRepository = followedComicRepository
                )
            }
        }
    }
}


/**
 * update reading progress of the followed comic when reading chapter changes
 */
private suspend fun updateReadingProgressWhenChapterChanges(
    readerUiState: MutableStateFlow<ReaderUiState>,
    chapterFetchedFirstTime: MutableStateFlow<Boolean>,
    followedComicRepository: FollowedComicRepository,
) {
    readerUiState.filterIsInstance(ReaderUiState.Success::class)
        .filter { it.followedComic != null }
        .distinctUntilChangedBy { it.readingChapter.chapter.hid }
        .collectLatest {
            // when a user entered the reader view for the first time, jump to the last reading page
            if (!chapterFetchedFirstTime.value) {
                chapterFetchedFirstTime.value = true
                val currentState = it
                it.followedComic?.readingChapter?.readingChapterCurrPage?.let {
                    currentState.setCurrentPage(it)
                }
                return@collectLatest
            }

            if (it.followedComic == null) {
                return@collectLatest
            }

            val readingChapter = it.readingChapter
            val readingProgress = FollowedComicReadingChapter(
                readingChapterHid = readingChapter.chapter.hid,
                readingChapterNumber = readingChapter.chapter.chapter ?: "",
                readingChapterCurrPage = 0,
                readingChapterTotalPage = readingChapter.chapter.images.size,
                nextChapterHid = readingChapter.next?.hid ?: ""
            )
            val followedComic = it.followedComic!!.copy(readingChapter = readingProgress)

            followedComicRepository.updateFollowedComic(comic = followedComic)
        }
}


sealed interface ReaderUiState {
    object Loading : ReaderUiState

    data class Error(val message: String? = null) : ReaderUiState

    data class Success(
        val readingChapter: ReadingChapter,
        var followedComic: FollowedComic?,
        val viewModelScope: CoroutineScope,
        private val userPreferencesRepository: UserPreferencesRepository,
        private val followedComicRepository: FollowedComicRepository,
    ) : ReaderUiState {
        private val userPreferences = userPreferencesRepository.userPreferences

        var currentPage = MutableStateFlow(0)

        var readingMode = userPreferences.map { it.readingMode }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ReadingMode.LeftToRight
        )

        val volumeKeysNavigation = userPreferences.map { it.volumeKeysNavigation }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

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

        suspend fun setCurrentPage(value: Int) {
            currentPage.value = value

            // if the `followedComic` exists, update its reading progress
            followedComic?.let {
                val readingProgress = FollowedComicReadingChapter(
                    readingChapterHid = readingChapter.chapter.hid,
                    readingChapterNumber = readingChapter.chapter.chapter ?: "",
                    readingChapterCurrPage = value,
                    readingChapterTotalPage = readingChapter.chapter.images.size,
                    nextChapterHid = readingChapter.next?.hid ?: ""
                )
                followedComic = it.copy(readingChapter = readingProgress)
                followedComicRepository.updateFollowedComic(comic = followedComic!!)
            }
        }

        suspend fun setReadingMode(value: ReadingMode) =
            userPreferencesRepository.setReadingMode(value)
    }
}