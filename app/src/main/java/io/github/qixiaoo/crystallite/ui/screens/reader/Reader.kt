package io.github.qixiaoo.crystallite.ui.screens.reader

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.qixiaoo.crystallite.data.model.ReadingMode
import io.github.qixiaoo.crystallite.ui.components.ErrorMessage
import io.github.qixiaoo.crystallite.ui.components.reader.ReaderHud
import io.github.qixiaoo.crystallite.ui.components.reader.SinglePageReader
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
    var isHudVisible by remember { readerUiState.isHudVisible }

    val imageList = readerUiState.imageList
    val readingModeValue = readingMode.value
    val readingChapter = readerUiState.readingChapter.chapter

    val direction = when (readingModeValue) {
        ReadingMode.LeftToRight -> LayoutDirection.Ltr
        ReadingMode.RightToLeft -> LayoutDirection.Rtl
    }

    CompositionLocalProvider(LocalLayoutDirection provides direction) {
        SinglePageReader(
            current = current.value,
            imageList = imageList,
            onClick = { isHudVisible = !isHudVisible },
            onPageChange = { coroutineScope.launch { readerUiState.setCurrentPage(it) } }
        )
    }

    ReaderHud(
        current = current.value,
        pageCount = imageList.size,
        title = readingChapter.mdComic?.title,
        chapter = readingChapter.chapter,
        chapterTitle = readingChapter.title,
        readingMode = readingModeValue,
        onPageChange = { coroutineScope.launch { readerUiState.setCurrentPage(it) } },
        isReaderHudVisible = isHudVisible,
        enablePreviousChapter = readerUiState.isPrevChapterEnabled,
        enableNextChapter = readerUiState.isNextChapterEnabled,
        onNavigateToPrevChapter = { coroutineScope.launch { readerViewModel.navigateToPrevChapter() } },
        onNavigateToNextChapter = { coroutineScope.launch { readerViewModel.navigateToNextChapter() } },
        onReadingModeChange = { coroutineScope.launch { readerUiState.setReadingMode(it) } },
        onNavigateBack = onNavigateBack
    )
}
