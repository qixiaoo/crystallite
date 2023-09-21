package io.github.qixiaoo.crystallite.ui.components.reader

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import io.github.qixiaoo.crystallite.data.model.ReadingMode
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme


@Composable
fun ReaderHud(
    modifier: Modifier = Modifier,
    current: Int,
    pageCount: Int,
    readingMode: ReadingMode,
    title: String? = "",
    chapter: String? = "",
    chapterTitle: String? = "",
    onPageChange: (Int) -> Unit,
    isReaderHudVisible: Boolean,
    enablePreviousChapter: Boolean = true,
    enableNextChapter: Boolean = true,
    onNavigateToPrevChapter: () -> Unit = {},
    onNavigateToNextChapter: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onReadingModeChange: (ReadingMode) -> Unit = {},
) {
    val topPadding by animateDpAsState(
        if (isReaderHudVisible) 0.dp else (-65).dp,
        label = "page top bar top padding"
    )

    val bottomPadding by animateDpAsState(
        if (isReaderHudVisible) 10.dp else (-50).dp,
        label = "page navigator bottom padding"
    )

    val direction = remember(readingMode) {
        when (readingMode) {
            ReadingMode.LeftToRight -> LayoutDirection.Ltr
            ReadingMode.RightToLeft -> LayoutDirection.Rtl
        }
    }

    val pageTopBarId = "pageTopBarId"
    val pageNavigatorId = "pageNavigatorId"

    val constraintSet = ConstraintSet {
        val pageTopBar = createRefFor(pageTopBarId)
        val pageNavigator = createRefFor(pageNavigatorId)

        constrain(pageTopBar) {
            top.linkTo(anchor = parent.top, margin = topPadding)
        }

        constrain(pageNavigator) {
            bottom.linkTo(anchor = parent.bottom, margin = bottomPadding)
        }
    }

    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        PageTopBar(
            title = title,
            chapter = chapter,
            chapterTitle = chapterTitle,
            readingMode = readingMode,
            onNavigateBack = onNavigateBack,
            onReadingModeChange = onReadingModeChange,
            modifier = Modifier.layoutId(pageTopBarId)
        )

        CompositionLocalProvider(LocalLayoutDirection provides direction) {
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
}


@Preview(showBackground = true)
@Composable
fun ReaderHudPreview() {
    CrystalliteTheme {
        ReaderHud(
            current = 0,
            pageCount = 20,
            readingMode = ReadingMode.LeftToRight,
            onPageChange = {},
            isReaderHudVisible = true
        )
    }
}