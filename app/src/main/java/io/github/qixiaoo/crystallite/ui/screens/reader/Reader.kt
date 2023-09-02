package io.github.qixiaoo.crystallite.ui.screens.reader

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.qixiaoo.crystallite.ui.components.ErrorMessage
import io.github.qixiaoo.crystallite.ui.components.reader.PageNavigator
import io.github.qixiaoo.crystallite.ui.components.reader.SinglePageReader
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme
import kotlinx.coroutines.launch


@Composable
internal fun Reader(readerViewModel: ReaderViewModel = hiltViewModel()) {
    val chapterState by readerViewModel.chapterUiState.collectAsStateWithLifecycle()

    when (chapterState) {
        is ChapterUiState.Error -> ErrorMessage(message = (chapterState as ChapterUiState.Error).message)
        is ChapterUiState.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        is ChapterUiState.Success -> {
            val readingChapter = (chapterState as ChapterUiState.Success).chapter
            val imageList = readingChapter.chapter.images.map { it.url }
            ReadingChapter(imageList = imageList)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReadingChapter(imageList: List<String>) {
    val coroutineScope = rememberCoroutineScope()
    val singlePageState = rememberPagerState(initialPage = 0) { imageList.size }

    var isPageNavigatorVisible by remember { mutableStateOf(false) }

    SinglePageReader(
        pagerState = singlePageState,
        imageList = imageList,
        onClick = { isPageNavigatorVisible = !isPageNavigatorVisible }
    )

    ReadingChapterHud(
        current = singlePageState.currentPage,
        pageCount = imageList.size,
        onPageChange = {
            coroutineScope.launch {
                singlePageState.scrollToPage(it)
            }
        },
        isPageNavigatorVisible = isPageNavigatorVisible
    )
}


@Composable
private fun ReadingChapterHud(
    current: Int,
    pageCount: Int,
    onPageChange: (Int) -> Unit,
    isPageNavigatorVisible: Boolean,
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
            modifier = Modifier.layoutId(pageNavigatorId)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ReadingChapterPreview() {
    CrystalliteTheme {
        ReadingChapter(
            imageList = listOf(
                "https://meo.comick.pictures/1-uBH4-P_O1_f7S.jpg",
                "https://meo.comick.pictures/2-IeaFh4XcsBWFY.png",
                "https://meo.comick.pictures/3-w-7AxbLtPTgqR.png",
            )
        )
    }
}