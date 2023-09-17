package io.github.qixiaoo.crystallite.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.common.GRID_ROW_ITEM_COUNT
import io.github.qixiaoo.crystallite.data.model.Comic
import io.github.qixiaoo.crystallite.data.model.TopComics
import io.github.qixiaoo.crystallite.ui.common.getCoverUrl
import io.github.qixiaoo.crystallite.ui.components.ErrorMessage
import io.github.qixiaoo.crystallite.ui.components.OrderedCover
import io.github.qixiaoo.crystallite.ui.components.RecentComics


@Composable
internal fun Home(
    onComicClick: (slug: String) -> Unit, homeViewModel: HomeViewModel = hiltViewModel()
) {
    val topComicsState by homeViewModel.topComicsUiState.collectAsStateWithLifecycle()
    val news by homeViewModel.news.collectAsStateWithLifecycle()
    val completions by homeViewModel.completions.collectAsStateWithLifecycle()

    when (topComicsState) {
        is TopComicsUiState.Error -> ErrorMessage(message = (topComicsState as TopComicsUiState.Error).message)
        TopComicsUiState.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        is TopComicsUiState.Success -> HomeContent(
            topComics = (topComicsState as TopComicsUiState.Success).topComics,
            news = news,
            completions = completions,
            onComicClick = onComicClick,
        )
    }
}


@Composable
private fun HomeContent(
    topComics: TopComics,
    news: List<List<Comic>>,
    completions: List<List<Comic>>,
    onComicClick: (slug: String) -> Unit,
) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val padding = 20.dp
    val spaceBetween = 30.dp
    val gridRowTopPadding = 15.dp

    val coverRatio = (3f / 4f)
    val reservedGapForGridItem = padding * 2
    val gridItemWidth = (screenWidth - padding * 2 - reservedGapForGridItem) / GRID_ROW_ITEM_COUNT
    val gridItemHeight = gridItemWidth / coverRatio

    LazyColumn {
        item {
            RecentComics(
                comics = topComics.trending,
                title = stringResource(id = R.string.most_viewed),
                padding = padding,
                onClick = { comic, _ -> onComicClick(comic.slug) },
                modifier = Modifier.padding(top = spaceBetween)
            )
        }

        item {
            RecentComics(
                comics = topComics.topFollowNewComics,
                title = stringResource(id = R.string.popular_new_comics),
                padding = padding,
                onClick = { comic, _ -> onComicClick(comic.slug) },
                modifier = Modifier.padding(top = spaceBetween)
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.recently_added),
                color = colorScheme.onBackground,
                style = typography.titleLarge,
                modifier = Modifier.padding(start = padding, end = padding, top = spaceBetween)
            )
        }

        items(items = news) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(gridItemHeight + gridRowTopPadding)
                    .padding(start = padding, end = padding, top = gridRowTopPadding)
            ) {
                it.forEach { comic ->
                    OrderedCover(
                        title = comic.title,
                        seqNo = null,
                        cover = getCoverUrl(comic),
                        onClick = { onComicClick(comic.slug) },
                    )
                }
            }
        }

        item {
            Text(
                text = stringResource(id = R.string.complete_series),
                color = colorScheme.onBackground,
                style = typography.titleLarge,
                modifier = Modifier.padding(start = padding, end = padding, top = spaceBetween)
            )
        }

        itemsIndexed(items = completions) { index, comics ->
            val gridRowBottomPadding = if (index == completions.lastIndex) spaceBetween else 0.dp

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(gridItemHeight + gridRowTopPadding + gridRowBottomPadding)
                    .padding(
                        start = padding,
                        end = padding,
                        top = gridRowTopPadding,
                        bottom = gridRowBottomPadding
                    )
            ) {
                comics.forEach { comic ->
                    OrderedCover(
                        title = comic.title,
                        seqNo = null,
                        cover = getCoverUrl(comic),
                        onClick = { onComicClick(comic.slug) },
                    )
                }
            }
        }
    }
}
