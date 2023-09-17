package io.github.qixiaoo.crystallite.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.data.model.Comic
import io.github.qixiaoo.crystallite.data.model.ComicListOrderedByPeriod
import io.github.qixiaoo.crystallite.data.model.ContentRating
import io.github.qixiaoo.crystallite.data.model.MdCover
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme
import kotlinx.coroutines.launch


@Composable
fun RecentComics(
    modifier: Modifier = Modifier,
    comics: ComicListOrderedByPeriod,
    title: String,
    padding: Dp = 0.dp,
    onClick: (comic: Comic, index: Int) -> Unit = { _, _ -> },
) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    var period by rememberSaveable {
        mutableIntStateOf(0)
    }

    var filtersExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()
    val comicCarouselState = rememberLazyListState()


    val week = stringResource(id = R.string.week)
    val month = stringResource(id = R.string.month)
    val quarter = stringResource(id = R.string.quarter)

    val filters = listOf(week, month, quarter)
    val comicsList = arrayOf(comics.week, comics.month, comics.quarter)

    Column(verticalArrangement = Arrangement.spacedBy(15.dp), modifier = Modifier.then(modifier)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding)
        ) {
            // title
            Text(
                text = title,
                color = colorScheme.onBackground,
                style = typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            // filters
            Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                // trigger
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "resume",
                    modifier = Modifier.clickable { filtersExpanded = !filtersExpanded }
                )

                // menu
                DropdownMenu(
                    expanded = filtersExpanded,
                    onDismissRequest = { filtersExpanded = false }
                ) {
                    for ((index, it) in filters.withIndex()) {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                period = index
                                coroutineScope.launch {
                                    comicCarouselState.animateScrollToItem(index = 0)
                                }
                            },
                            trailingIcon = {
                                if (period == index) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "filter checked"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

        // comics to be displayed
        ComicCarousel(
            comics = comicsList[period],
            contentPadding = PaddingValues(horizontal = padding),
            state = comicCarouselState,
            onClick = onClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecentComicsPreview() {
    val comic = Comic(
        id = null,
        slug = "",
        title = "Yu-Gi-Oh!",
        genres = emptyList(),
        demoGraphic = null,
        contentRating = ContentRating.SAFE,
        lastChapter = null,
        mdCovers = listOf(MdCover(400, 500, "A0B5X.jpg")),
        createdAt = null,
        uploadedAt = null,
        followerCount = null
    )
    val list = (0 until 6).map { i ->
        val title = comic.title.plus(i.toString())
        comic.copy(title = title)
    }
    val comics = ComicListOrderedByPeriod(
        week = list,
        month = list,
        quarter = list,
        halfYear = null,
        threeQuarters = null,
        year = null,
        twoYears = null
    )

    CrystalliteTheme {
        RecentComics(comics = comics, title = "Popular New Comics")
    }
}