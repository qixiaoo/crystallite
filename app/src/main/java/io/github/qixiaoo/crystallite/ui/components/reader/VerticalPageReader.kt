package io.github.qixiaoo.crystallite.ui.components.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.zoomable


@Composable
fun VerticalPageReader(
    modifier: Modifier = Modifier,
    imageList: List<String>,
    state: LazyListState = rememberLazyListState(),
    onClick: () -> Unit = {},
) {
    val typography = MaterialTheme.typography
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val fullScreenModifier = remember(screenWidthDp, screenHeightDp) {
        Modifier
            .width(screenWidthDp.dp)
            .height(screenHeightDp.dp)
    }

    LazyColumn(
        state = state,
        modifier = Modifier
            .zoomable(state = rememberZoomableState(), onClick = { onClick() })
            .then(modifier)
    ) {
        itemsIndexed(items = imageList, key = { _, image -> image }) { pageIndex, image ->
            SubcomposeAsyncImage(
                model = image,
                loading = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = fullScreenModifier
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                        )
                    }
                },
                success = {
                    AsyncImage(
                        model = this.painter.request.data,
                        contentDescription = "page ${pageIndex + 1}",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .background(Color.Black)
                            .width(screenWidthDp.dp)
                    )
                },
                error = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = fullScreenModifier
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
}


@Preview(showBackground = true)
@Composable
fun VerticalPageReaderPreview() {
    CrystalliteTheme {
        VerticalPageReader(
            imageList = listOf(
                "1.jpg",
                "2.jpg",
                "3.jpg",
            )
        )
    }
}
