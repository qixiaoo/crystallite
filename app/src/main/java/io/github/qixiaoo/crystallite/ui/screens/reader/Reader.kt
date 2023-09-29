package io.github.qixiaoo.crystallite.ui.screens.reader

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.qixiaoo.crystallite.data.model.ReadingMode
import io.github.qixiaoo.crystallite.ui.components.ErrorMessage
import io.github.qixiaoo.crystallite.ui.components.reader.HorizontalPageReader
import io.github.qixiaoo.crystallite.ui.components.reader.ReaderHud
import io.github.qixiaoo.crystallite.ui.components.reader.VerticalPageReader
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@Composable
internal fun Reader(
    onNavigateBack: () -> Unit,
    readerViewModel: ReaderViewModel = hiltViewModel(),
) {
    val readerUiState by readerViewModel.readerUiState.collectAsStateWithLifecycle()

    when (readerUiState) {
        is ReaderUiState.Error -> ErrorMessage(message = (readerUiState as ReaderUiState.Error).message)
        is ReaderUiState.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        is ReaderUiState.Success -> {
            ReaderContent(
                readerUiState = readerUiState as ReaderUiState.Success,
                readerViewModel = readerViewModel,
                onNavigateBack = onNavigateBack
            )
        }
    }
}


@Composable
private fun ReaderContent(
    readerUiState: ReaderUiState.Success,
    readerViewModel: ReaderViewModel,
    onNavigateBack: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val current = readerUiState.currentPage.collectAsStateWithLifecycle()
    val readingMode = readerUiState.readingMode.collectAsStateWithLifecycle()
    val volumeKeysNavigation = readerUiState.volumeKeysNavigation.collectAsStateWithLifecycle()
    var isHudVisible by remember { readerUiState.isHudVisible }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val currentPage = current.value
    val imageList = readerUiState.imageList
    val readingModeValue = readingMode.value
    val readingChapter = readerUiState.readingChapter.chapter

    val pageReaderState = rememberLazyListState(initialFirstVisibleItemIndex = currentPage)
    val onPageChange: (Int) -> Unit = remember(readerUiState, pageReaderState, readingModeValue) {
        {
            coroutineScope.launch {
                readerUiState.setCurrentPage(it)
                if (readingModeValue == ReadingMode.ContinuousVertical) {
                    pageReaderState.scrollToItem(it)
                }
            }
        }
    }

    PageReader(
        state = pageReaderState,
        current = currentPage,
        imageList = imageList,
        readingMode = readingModeValue,
        onClick = { isHudVisible = !isHudVisible },
        onPageChange = {
            coroutineScope.launch {
                // when the `PageReader` is in the continuous vertical mode,
                // no need to call `pageReaderState.scrollToItem`, otherwise the fling animation of the `LazyColumn` will stop
                readerUiState.setCurrentPage(it)
            }
        }
    )

    ReaderHud(
        current = currentPage,
        pageCount = imageList.size,
        title = readingChapter.mdComic?.title,
        chapter = readingChapter.chapter,
        chapterTitle = readingChapter.title,
        readingMode = readingModeValue,
        onPageChange = onPageChange,
        isReaderHudVisible = isHudVisible,
        enablePreviousChapter = readerUiState.isPrevChapterEnabled,
        enableNextChapter = readerUiState.isNextChapterEnabled,
        onNavigateToPrevChapter = { coroutineScope.launch { readerViewModel.navigateToPrevChapter() } },
        onNavigateToNextChapter = { coroutineScope.launch { readerViewModel.navigateToNextChapter() } },
        onReadingModeChange = { coroutineScope.launch { readerUiState.setReadingMode(it) } },
        onNavigateBack = onNavigateBack,
        modifier = Modifier
            .focusRequester(focusRequester)
            .focusable()
            .volumeKeysNavigation(
                currentPage = currentPage,
                totalPage = imageList.size,
                volumeKeysNavigationEnabled = volumeKeysNavigation.value,
                onPageChange = onPageChange
            )
    )
}


@Composable
private fun PageReader(
    state: LazyListState,
    current: Int,
    imageList: List<String>,
    readingMode: ReadingMode,
    onClick: () -> Unit = {},
    onPageChange: (Int) -> Unit = {},
) {
    val direction = remember(readingMode) {
        when (readingMode) {
            ReadingMode.LeftToRight -> LayoutDirection.Ltr
            ReadingMode.RightToLeft -> LayoutDirection.Rtl
            else -> LayoutDirection.Ltr
        }
    }

    val updatedCurrent by rememberUpdatedState(current)
    val updatedOnPageChange by rememberUpdatedState(onPageChange)

    LaunchedEffect(readingMode, state) {
        if (readingMode != ReadingMode.ContinuousVertical) {
            return@LaunchedEffect
        }

        // when the `PageReader` is in the continuous vertical mode,
        // listen to the `firstVisibleItemIndex` changes and call `onPageChange`
        snapshotFlow { state.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect {
                updatedOnPageChange(it)
            }
    }

    LaunchedEffect(readingMode) {
        // when the readingMode changed to the `ReadingMode.ContinuousVertical`,
        // call the `scrollToItem` to scroll to the current page
        if (readingMode == ReadingMode.ContinuousVertical) {
            state.scrollToItem(index = updatedCurrent)
        }
    }

    if (readingMode == ReadingMode.LeftToRight || readingMode == ReadingMode.RightToLeft) {
        CompositionLocalProvider(LocalLayoutDirection provides direction) {
            HorizontalPageReader(
                current = updatedCurrent,
                imageList = imageList,
                onClick = onClick,
                onPageChange = updatedOnPageChange
            )
        }
    }

    if (readingMode == ReadingMode.ContinuousVertical) {
        VerticalPageReader(
            state = state,
            imageList = imageList,
            onClick = onClick,
        )
    }
}


/**
 * turn pages when volume keys event triggered
 */
private fun Modifier.volumeKeysNavigation(
    currentPage: Int,
    totalPage: Int,
    volumeKeysNavigationEnabled: Boolean,
    onPageChange: (Int) -> Unit,
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "volumeKeysNavigation"
        value = volumeKeysNavigationEnabled
    }
) {
    val current by rememberUpdatedState(currentPage)
    val total by rememberUpdatedState(totalPage)
    val volumeKeysNavigation by rememberUpdatedState(volumeKeysNavigationEnabled)
    val onPageChangeFn by rememberUpdatedState(onPageChange)

    val modifier = remember {
        Modifier.onKeyEvent {
            // do nothing when `volumeKeysNavigation` disabled
            if (!volumeKeysNavigation) {
                return@onKeyEvent false
            }

            if (it.type == KeyEventType.KeyUp && it.key == Key.VolumeUp) {
                val prevPage = (current - 1)
                    .coerceAtLeast(0)
                    .coerceAtMost(total - 1)
                onPageChangeFn(prevPage)
            }

            if (it.type == KeyEventType.KeyUp && it.key == Key.VolumeDown) {
                val nextPage = (current + 1)
                    .coerceAtLeast(0)
                    .coerceAtMost(total - 1)
                onPageChangeFn(nextPage)
            }

            // consume `VolumeUp` and `VolumeDown` event
            return@onKeyEvent (it.key == Key.VolumeUp || it.key == Key.VolumeDown)
        }
    }

    return@composed this then modifier
}
