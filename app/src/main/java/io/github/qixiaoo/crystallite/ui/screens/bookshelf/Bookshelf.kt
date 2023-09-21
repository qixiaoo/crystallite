package io.github.qixiaoo.crystallite.ui.screens.bookshelf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.common.GRID_ROW_ITEM_COUNT
import io.github.qixiaoo.crystallite.data.model.FollowedComic
import io.github.qixiaoo.crystallite.ui.common.getCoverUrl
import io.github.qixiaoo.crystallite.ui.components.CenteredMessage
import io.github.qixiaoo.crystallite.ui.components.ErrorMessage
import io.github.qixiaoo.crystallite.ui.components.OrderedCover
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme


@Composable
internal fun Bookshelf(
    onComicClick: (slug: String) -> Unit,
    bookshelfViewModel: BookshelfViewModel = hiltViewModel(),
) {
    val bookshelfUiState by bookshelfViewModel.bookshelfUiState.collectAsStateWithLifecycle()

    when (bookshelfUiState) {
        is BookshelfUiState.Error -> ErrorMessage(message = (bookshelfUiState as BookshelfUiState.Error).message)
        BookshelfUiState.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        is BookshelfUiState.Success -> BookshelfContent(
            followedComics = (bookshelfUiState as BookshelfUiState.Success).followedComics,
            onComicClick = onComicClick
        )
    }
}


@Composable
private fun BookshelfContent(
    followedComics: List<FollowedComic>,
    onComicClick: (slug: String) -> Unit = {},
) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val chunkedFollowedComic = remember(followedComics) {
        followedComics.chunked(GRID_ROW_ITEM_COUNT)
    }

    val padding = 20.dp
    val titleTopPadding = 30.dp + 8.dp
    val gridRowTopPadding = 15.dp

    val coverRatio = (3f / 4f)
    val gridItemGap = 20.dp
    val reservedGapForGridItem = gridItemGap * 2
    val gridItemWidth = (screenWidth - padding * 2 - reservedGapForGridItem) / GRID_ROW_ITEM_COUNT
    val gridItemHeight = gridItemWidth / coverRatio

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (followedComics.isEmpty()) {
            item {
                CenteredMessage(
                    message = stringResource(R.string.empty),
                    modifier = Modifier.fillParentMaxHeight()
                )
            }
            return@LazyColumn
        }

        item {
            Text(
                text = stringResource(id = R.string.library),
                color = colorScheme.onBackground,
                style = typography.titleLarge,
                modifier = Modifier.padding(start = padding, end = padding, top = titleTopPadding)
            )
        }

        items(items = chunkedFollowedComic) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = gridItemGap),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(gridItemHeight + gridRowTopPadding)
                    .padding(start = padding, end = padding, top = gridRowTopPadding)
            ) {
                it.forEach { comic ->
                    OrderedCover(
                        title = comic.title,
                        seqNo = null,
                        cover = getCoverUrl(comic.mdCover),
                        onClick = { onComicClick(comic.slug) },
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.padding(vertical = gridRowTopPadding))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BookshelfContentPreview() {
    CrystalliteTheme {
        BookshelfContent(
            followedComics = emptyList()
        )
    }
}
