package io.github.qixiaoo.crystallite.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.logic.model.Comic
import io.github.qixiaoo.crystallite.logic.model.ComicListOrderedByPeriod
import io.github.qixiaoo.crystallite.logic.model.ContentRating
import io.github.qixiaoo.crystallite.logic.model.MdCover
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun CompactFilterChip(selected: Boolean, label: String, onClick: () -> Unit) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    val transition = updateTransition(selected, label = "select label")

    val color by transition.animateColor(label = "font color") {
        when (it) {
            true -> colorScheme.background
            false -> colorScheme.onBackground
        }
    }

    val bgColor by transition.animateColor(label = "background  color") {
        when (it) {
            true -> colorScheme.onBackground
            false -> colorScheme.background
        }
    }

    Text(
        text = label,
        style = typography.labelSmall,
        color = color,
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .clickable(onClick = onClick)
            .background(color = bgColor)
            .padding(6.dp, 3.dp)
    )
}


@Composable
fun CompactFilterChips(items: List<String>, current: Int, onSelect: (Int) -> Unit) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.border(
            width = 1.dp, color = colorScheme.secondary, shape = RoundedCornerShape(50.dp)
        )
    ) {
        items.forEachIndexed { index, item ->
            CompactFilterChip(selected = index == current,
                label = item,
                onClick = { onSelect(index) })
        }
    }
}


@Composable
fun RecentComics(
    modifier: Modifier = Modifier,
    comics: ComicListOrderedByPeriod,
    title: String,
    padding: Dp = 0.dp
) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    var period by rememberSaveable {
        mutableStateOf(0)
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
            Text(text = title, color = colorScheme.onBackground, style = typography.titleMedium)

            // filters
            CompactFilterChips(items = filters, current = period) {
                period = it
                coroutineScope.launch {
                    delay(500)
                    comicCarouselState.animateScrollToItem(index = 0)
                }
            }
        }

        // comics to be displayed
        ComicCarousel(
            comics = comicsList[period],
            contentPadding = PaddingValues(horizontal = padding),
            state = comicCarouselState
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