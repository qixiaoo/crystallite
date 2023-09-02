package io.github.qixiaoo.crystallite.ui.components.reader

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme
import me.saket.telephoto.zoomable.coil.ZoomableAsyncImage


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SinglePageReader(
    modifier: Modifier = Modifier,
    imageList: List<String>,
    pagerState: PagerState = rememberPagerState(initialPage = 0) { imageList.size },
    readingMode: ReadingMode = ReadingMode.LeftToRight,
    onClick: (() -> Unit)? = null,
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val direction = when (readingMode) {
        ReadingMode.LeftToRight -> LayoutDirection.Ltr
        ReadingMode.RightToLeft -> LayoutDirection.Rtl
    }

    CompositionLocalProvider(LocalLayoutDirection provides direction) {
        HorizontalPager(
            state = pagerState,
            beyondBoundsPageCount = 1,
            modifier = modifier
        ) { page ->
            val imageUrl = imageList.getOrNull(page) ?: ""

            ZoomableAsyncImage(
                model = imageUrl,
                contentDescription = "page ${page + 1}",
                contentScale = ContentScale.Fit,
                onClick = { onClick?.invoke() },
                modifier = Modifier
                    .background(Color.Black)
                    .width(screenWidthDp.dp)
                    .height(screenHeightDp.dp)
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun SinglePageReaderPreview() {
    CrystalliteTheme {
        SinglePageReader(
            imageList = listOf(
                "https://meo.comick.pictures/1-uBH4-P_O1_f7S.jpg",
                "https://meo.comick.pictures/2-IeaFh4XcsBWFY.png",
                "https://meo.comick.pictures/3-w-7AxbLtPTgqR.png",
            )
        )
    }
}
