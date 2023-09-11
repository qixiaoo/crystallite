package io.github.qixiaoo.crystallite.ui.screens.reader

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.qixiaoo.crystallite.ui.components.ErrorMessage
import io.github.qixiaoo.crystallite.ui.components.reader.PageNavigator
import io.github.qixiaoo.crystallite.ui.components.reader.ReadingMode
import io.github.qixiaoo.crystallite.ui.components.reader.SinglePageReader


@Composable
internal fun Reader(readerViewModel: ReaderViewModel = hiltViewModel()) {
    val readerUiState by readerViewModel.readerUiState.collectAsStateWithLifecycle()

    when (readerUiState) {
        is ReaderUiState.Error -> ErrorMessage(message = (readerUiState as ReaderUiState.Error).message)
        is ReaderUiState.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        is ReaderUiState.Success -> {
            ReaderContent(
                readerUiState = readerUiState as ReaderUiState.Success,
                readerViewModel = readerViewModel
            )
        }
    }
}


@Composable
private fun ReaderContent(readerUiState: ReaderUiState.Success, readerViewModel: ReaderViewModel) {
    val current = readerUiState.currentPage.collectAsStateWithLifecycle()
    var isHudVisible by remember { readerUiState.isHudVisible }

    val imageList = readerUiState.imageList
    val readingMode = ReadingMode.RightToLeft

    val direction = when (readingMode) {
        ReadingMode.LeftToRight -> LayoutDirection.Ltr
        ReadingMode.RightToLeft -> LayoutDirection.Rtl
    }

    CompositionLocalProvider(LocalLayoutDirection provides direction) {
        SinglePageReader(
            current = current.value,
            imageList = imageList,
            onClick = { isHudVisible = !isHudVisible },
            onPageChange = { readerUiState.setCurrentPage(it) }
        )

        ReaderHud(
            current = current.value,
            pageCount = imageList.size,
            onPageChange = { readerUiState.setCurrentPage(it) },
            isPageNavigatorVisible = isHudVisible,
            enablePreviousChapter = readerUiState.isPrevChapterEnabled,
            enableNextChapter = readerUiState.isNextChapterEnabled,
            onNavigateToPrevChapter = readerViewModel::navigateToPrevChapter,
            onNavigateToNextChapter = readerViewModel::navigateToNextChapter
        )
    }
}


@Composable
private fun ReaderHud(
    current: Int,
    pageCount: Int,
    onPageChange: (Int) -> Unit,
    isPageNavigatorVisible: Boolean,
    enablePreviousChapter: Boolean = true,
    enableNextChapter: Boolean = true,
    onNavigateToPrevChapter: () -> Unit = {},
    onNavigateToNextChapter: () -> Unit = {},
) {
    val pageNavigatorPadding = 10.dp

    val bottomPadding by animateDpAsState(
        if (isPageNavigatorVisible) pageNavigatorPadding else (-50).dp,
        label = "page navigator bottom padding"
    )

    val pageNavigatorId = "pageNavigatorId"

    val constraintSet = ConstraintSet {
        val pageNavigator = createRefFor(pageNavigatorId)

        constrain(pageNavigator) {
            bottom.linkTo(anchor = parent.bottom, margin = bottomPadding)
        }
    }

    ConstraintLayout(constraintSet = constraintSet, modifier = Modifier.fillMaxSize()) {
        PageNavigator(
            current = current,
            pageCount = pageCount,
            onPageChange = onPageChange,
            margin = PaddingValues(horizontal = 10.dp),
            enablePreviousChapter = enablePreviousChapter,
            enableNextChapter = enableNextChapter,
            onNavigateToPrevChapter = onNavigateToPrevChapter,
            onNavigateToNextChapter = onNavigateToNextChapter,
            modifier = Modifier.layoutId(pageNavigatorId)
        )
    }
}
