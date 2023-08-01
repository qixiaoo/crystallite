package io.github.qixiaoo.crystallite.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.logic.model.Comic
import io.github.qixiaoo.crystallite.logic.model.ContentRating
import io.github.qixiaoo.crystallite.logic.model.MdCover
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme


@Composable
fun ComicCarousel(
    modifier: Modifier = Modifier,
    comics: List<Comic>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState(),
) {
    val prefix = stringResource(R.string.pictures_site)

    val horizontalArrangement = 10.dp
    val height = 144.dp

    LazyRow(
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(horizontalArrangement),
        modifier = Modifier
            .height(height)
            .then(modifier)
    ) {
        itemsIndexed(comics, key = { _, item -> item.title }) { index, item ->
            val firstCover = item.mdCovers.getOrNull(0)?.b2key
            val cover = if (firstCover == null) null else prefix.plus(firstCover)
            OrderedCover(
                title = item.title,
                seqNo = index + 1,
                cover = cover,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComicCarouselPreview() {
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

    CrystalliteTheme {
        ComicCarousel(comics = list)
    }
}
