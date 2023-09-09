package io.github.qixiaoo.crystallite.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.data.model.ContentRating
import io.github.qixiaoo.crystallite.data.model.MdCover
import io.github.qixiaoo.crystallite.data.model.ProgressStatus
import io.github.qixiaoo.crystallite.data.model.SearchResultComic
import io.github.qixiaoo.crystallite.data.model.SearchResultType
import io.github.qixiaoo.crystallite.ui.common.getCoverUrl
import io.github.qixiaoo.crystallite.ui.common.toResourceId
import io.github.qixiaoo.crystallite.ui.components.ErrorMessage
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme


@Composable
internal fun Search(
    onComicClick: (slug: String) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    val type = searchViewModel.type.collectAsStateWithLifecycle()

    val comicListUiState = searchViewModel.comicList.collectAsStateWithLifecycle()

    val searchTypeList = listOf(SearchResultType.COMIC, SearchResultType.AUTHOR)
    val searchTypeTabList = listOf(
        stringResource(id = R.string.comic),
        stringResource(id = R.string.author)
    )
    val selectedTabIndex = searchTypeList.indexOf(type.value)

    Column {
        // search type tab row
        TabRow(selectedTabIndex = selectedTabIndex) {
            searchTypeTabList.forEachIndexed { index, searchType ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { searchViewModel.type.value = searchTypeList[index] },
                    text = {
                        Text(
                            text = searchType,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }

        // search result: comic
        if (type.value == SearchResultType.COMIC) {
            ComicTabContent(comicListUiState = comicListUiState.value, onComicClick = onComicClick)
        }

        // search result: author
        if (type.value == SearchResultType.AUTHOR) {
            AuthorTabContent()
        }
    }
}


@Composable
private fun ComicTabContent(
    comicListUiState: ComicListUiState,
    onComicClick: (slug: String) -> Unit,
) {
    when (comicListUiState) {
        ComicListUiState.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        is ComicListUiState.Error -> ErrorMessage(message = comicListUiState.message)

        is ComicListUiState.Success -> {
            val comicList = comicListUiState.comicList

            if (comicList.isEmpty()) {
                return CenteredMessage(message = stringResource(id = R.string.empty))
            }

            LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
                itemsIndexed(
                    items = comicList,
                    key = { _, it -> it.id }) { index, comic ->
                    Comic(
                        comic = comic,
                        isLast = index == comicList.lastIndex,
                        onClick = onComicClick
                    )
                }
            }
        }
    }
}


@Composable
private fun AuthorTabContent() {
    CenteredMessage(message = stringResource(id = R.string.not_implemented))
}


@Composable
private fun Comic(
    comic: SearchResultComic,
    isLast: Boolean = false,
    onClick: (slug: String) -> Unit = {},
) {
    val typography = MaterialTheme.typography

    val cover = getCoverUrl(comic.mdCovers)

    ElevatedCard(
        modifier = Modifier
            .padding(top = 10.dp, bottom = if (isLast) 10.dp else 0.dp)
            .fillMaxWidth()
            .clickable { onClick(comic.slug) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .height(80.dp)
        ) {
            AsyncImage(
                model = cover,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(3f / 4f)
            )

            Spacer(modifier = Modifier.padding(horizontal = 5.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = comic.title, maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.titleMedium
                )

                comic.rating?.let { Text(text = it, style = typography.bodyMedium) }

                comic.status?.let {
                    Text(
                        text = stringResource(id = it.toResourceId()),
                        style = typography.bodyMedium
                    )
                }

                comic.description?.let {
                    Text(
                        text = it,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun CenteredMessage(message: String) {
    val typography = MaterialTheme.typography

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = message, style = typography.bodyMedium)
    }
}


@Preview(showBackground = true)
@Composable
fun ComicPreview() {
    CrystalliteTheme {
        val comic = SearchResultComic(
            id = 63129,
            hid = "z_M1GL9Z",
            slug = "00-father-i-don-t-want-to-get-married",
            title = "Father, I Don't Want This Marriage!",
            genres = emptyList(),
            status = ProgressStatus.ONGOING,
            rating = "9.08",
            description = "I’m Juvelian? The daughter of the duke and the villainess of this novel?",
            contentRating = ContentRating.SAFE,
            lastChapter = 1f,
            mdCovers = listOf(MdCover(w = 1992, h = 2866, b2key = "zBQgK.jpg"))
        )
        Comic(comic)
    }
}