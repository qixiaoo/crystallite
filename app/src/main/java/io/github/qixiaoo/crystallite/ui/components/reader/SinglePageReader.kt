package io.github.qixiaoo.crystallite.ui.components.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme
import me.saket.telephoto.zoomable.coil.ZoomableAsyncImage


@Composable
fun SinglePageReader(
    modifier: Modifier = Modifier,
    current: Int = 0,
    imageList: List<String>,
    onClick: () -> Unit = {},
    onPageChange: (Int) -> Unit = {},
) {
    val typography = MaterialTheme.typography

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    HorizontalPager(
        modifier = modifier,
        currentPage = current,
        pageCount = imageList.size,
        onClick = onClick,
        onPageChange = onPageChange,
        pageKey = { imageList[it] }
    ) { pageIndex ->
        SubcomposeAsyncImage(
            model = imageList[pageIndex],
            loading = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                    )
                }
            },
            success = {
                // reset direction to LayoutDirection.Ltr to prevent zoomed image position error
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    ZoomableAsyncImage(
                        model = this.painter.request.data,
                        contentDescription = "page ${pageIndex + 1}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .background(Color.Black)
                            .width(screenWidthDp.dp)
                            .height(screenHeightDp.dp),
                    )
                }
            },
            error = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.failed_to_load),
                        style = typography.bodyMedium
                    )
                }
            },
            contentDescription = "page ${pageIndex + 1}"
        )
    }
}


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
