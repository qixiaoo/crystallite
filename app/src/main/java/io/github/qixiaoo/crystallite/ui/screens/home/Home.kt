package io.github.qixiaoo.crystallite.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.ui.common.GRID_ROW_ITEM_COUNT
import io.github.qixiaoo.crystallite.ui.common.getCoverUrl
import io.github.qixiaoo.crystallite.ui.components.OrderedCover
import io.github.qixiaoo.crystallite.ui.components.RecentComics


@Composable
internal fun Home(homeViewModel: HomeViewModel = hiltViewModel()) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val topComics by homeViewModel.topComics.collectAsStateWithLifecycle()
    val news by homeViewModel.news.collectAsStateWithLifecycle()
    val completions by homeViewModel.completions.collectAsStateWithLifecycle()

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
                modifier = Modifier.padding(top = spaceBetween)
            )
        }

        item {
            RecentComics(
                comics = topComics.topFollowNewComics,
                title = stringResource(id = R.string.popular_new_comics),
                padding = padding,
                modifier = Modifier.padding(top = spaceBetween)
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.recently_added),
                color = colorScheme.onBackground,
                style = typography.titleMedium,
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
                        title = comic.title, seqNo = null, cover = getCoverUrl(comic),
                    )
                }
            }
        }

        item {
            Text(
                text = stringResource(id = R.string.complete_series),
                color = colorScheme.onBackground,
                style = typography.titleMedium,
                modifier = Modifier.padding(start = padding, end = padding, top = spaceBetween)
            )
        }

        itemsIndexed(items = completions) { index, comics ->
            val gridRowBottomPadding = if (index == completions.size - 1) spaceBetween else 0.dp

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
                        title = comic.title, seqNo = null, cover = getCoverUrl(comic),
                    )
                }
            }
        }
    }
}
