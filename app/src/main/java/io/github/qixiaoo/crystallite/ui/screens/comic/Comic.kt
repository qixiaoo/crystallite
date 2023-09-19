package io.github.qixiaoo.crystallite.ui.screens.comic


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.data.model.ChapterDetail
import io.github.qixiaoo.crystallite.data.model.ProgressStatus
import io.github.qixiaoo.crystallite.ui.common.getCoverUrl
import io.github.qixiaoo.crystallite.ui.common.toResourceId
import io.github.qixiaoo.crystallite.ui.components.ErrorMessage
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


private val CHAPTER_HEIGHT = 56.dp

@Composable
internal fun Comic(
    onChapterClick: (chapterHid: String) -> Unit,
    comicViewModel: ComicViewModel = hiltViewModel(),
) {
    val comicState by comicViewModel.comicUiState.collectAsStateWithLifecycle()

    when (comicState) {
        is ComicUiState.Error -> ErrorMessage(message = (comicState as ComicUiState.Error).message)
        ComicUiState.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        is ComicUiState.Success -> {
            val comicStateSuccess = comicState as ComicUiState.Success
            val pagingChapters = comicStateSuccess.pagingChapters

            ComicDetail(
                comicState = comicStateSuccess,
                pagingChapters = pagingChapters,
                onChapterClick = { onChapterClick(it.hid) },
                onResume = {
                    onChapterClick(
                        comicStateSuccess.followedComic?.readingChapter?.readingChapterHid
                            ?: comicStateSuccess.comic.firstChap.hid
                    )
                }
            )
        }
    }
}

@Composable
private fun ComicDetail(
    comicState: ComicUiState.Success,
    pagingChapters: Flow<PagingData<ChapterDetail>>,
    onChapterClick: (chapter: ChapterDetail) -> Unit = {},
    onResume: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val chapterPagingItems = pagingChapters.collectAsLazyPagingItems()

    val comicDetail = comicState.comic
    val title = comicDetail.comic.title
    val authors = comicDetail.authors.map { it.name }
    val status = comicDetail.comic.status
    val cover = getCoverUrl(comicDetail.comic.mdCovers)
    val description = comicDetail.comic.description
    val followed = comicState.followed

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            // comic introduction
            item {
                ComicDetailHeader(
                    title = title,
                    authors = authors,
                    status = status,
                    cover = cover,
                    description = description,
                    followed = followed.value,
                    onFollowedChange = {
                        coroutineScope.launch {
                            comicState.toggleFollowedComic(isFollowed = it)
                        }
                    }
                )
            }

            // chapter list
            chapterPagingItems.chapterList(lazyListScope = this, onChapterClick = onChapterClick)
        }

        // resume button
        val resumeButton = createRef()
        FloatingActionButton(onClick = onResume, modifier = Modifier.constrainAs(resumeButton) {
            bottom.linkTo(anchor = parent.bottom, margin = 20.dp)
            end.linkTo(anchor = parent.end, margin = 20.dp)
        }) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "resume",
            )
        }
    }
}

@Composable
private fun ComicDetailHeader(
    title: String = "",
    authors: List<String> = emptyList(),
    status: ProgressStatus,
    cover: String? = "",
    description: String? = "",
    followed: Boolean = false,
    onFollowedChange: (Boolean) -> Unit = {},
) {
    val typography = MaterialTheme.typography
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val isBlurSupported = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S

    val coverRatio = (3f / 4f)
    val headImageHeight = screenHeight / 3f
    val headImageWidth = headImageHeight * coverRatio
    val contentHorizontalPadding = 20.dp
    val headBackgroundHeight = screenHeight * (2f / 5f)

    val joinedAuthors = authors.joinToString()
    val bookmarkIcon = if (followed) Icons.Filled.Bookmark else Icons.Outlined.BookmarkAdd

    val headImageId = "headImageId"
    val headBackgroundId = "headBackgroundId"
    val descriptionCardId = "descriptionCardId"

    val constraintSet = ConstraintSet {
        val headImage = createRefFor(headImageId)
        val headBackground = createRefFor(headBackgroundId)
        val descriptionCard = createRefFor(descriptionCardId)

        constrain(headImage) {
            centerTo(headBackground)
        }

        constrain(descriptionCard) {
            top.linkTo(anchor = headBackground.bottom, margin = 30.dp)
        }
    }

    ConstraintLayout(
        constraintSet = constraintSet, modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp)
    ) {
        // cover background
        AsyncImage(
            model = cover,
            contentDescription = "the cover of \"$title\"",
            contentScale = ContentScale.Crop,
            alpha = if (isBlurSupported) DefaultAlpha else 0.5f,
            modifier = Modifier
                .fillMaxWidth()
                .height(headBackgroundHeight)
                .layoutId(headBackgroundId)
                .blur(10.dp)
        )

        // cover
        ElevatedCard(
            modifier = Modifier.layoutId(headImageId),
            elevation = CardDefaults.elevatedCardElevation(15.dp)
        ) {
            ConstraintLayout {
                val (bookmark) = createRefs()

                AsyncImage(
                    model = cover,
                    contentDescription = "the cover of \"$title\"",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(headImageWidth)
                        .height(headImageHeight)

                )

                FilledIconToggleButton(
                    checked = followed,
                    onCheckedChange = onFollowedChange,
                    modifier = Modifier.constrainAs(bookmark) {
                        end.linkTo(parent.end)
                    }
                ) {
                    Icon(
                        imageVector = bookmarkIcon,
                        contentDescription = "bookmark",
                    )
                }
            }
        }

        // title, authors, description...
        ElevatedCard(
            elevation = CardDefaults.elevatedCardElevation(3.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = contentHorizontalPadding)
                .layoutId(descriptionCardId)
        ) {
            SelectionContainer {
                Column(modifier = Modifier.padding(25.dp)) {
                    Text(
                        text = title,
                        style = typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    if (joinedAuthors.isNotBlank()) {
                        Text(
                            text = joinedAuthors,
                            style = typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = stringResource(id = status.toResourceId()),
                        style = typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    if (!description.isNullOrBlank()) {
                        Text(
                            text = description,
                            style = typography.bodySmall,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ComicDetailHeaderPreview() {
    CrystalliteTheme {
        ComicDetailHeader(
            title = "title",
            authors = listOf("First", "Second"),
            status = ProgressStatus.COMPLETED,
            description = "Some description here.",
            followed = false
        )
    }
}


private fun LazyPagingItems<ChapterDetail>.chapterList(
    lazyListScope: LazyListScope,
    onChapterClick: (chapter: ChapterDetail) -> Unit = {},
) {
    val chapterPagingItems: LazyPagingItems<ChapterDetail> = this

    // chapter list
    lazyListScope.items(
        count = chapterPagingItems.itemCount,
        key = chapterPagingItems.itemKey { it.id },
    ) { index ->
        val chapterItem = chapterPagingItems[index]
        if (chapterItem == null) {
            // placeholder
            Column(modifier = Modifier.height(CHAPTER_HEIGHT), content = {})
        } else {
            Chapter(chapter = chapterItem, onChapterClick = onChapterClick)
        }
    }

    // loading indicator of the chapter list
    if (
        (chapterPagingItems.loadState.refresh is LoadState.Loading) ||
        (chapterPagingItems.loadState.append is LoadState.Loading)
    ) {
        lazyListScope.item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(CHAPTER_HEIGHT)
                    .fillMaxWidth()
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
private fun Chapter(chapter: ChapterDetail, onChapterClick: (chapter: ChapterDetail) -> Unit = {}) {
    val typography = MaterialTheme.typography

    val contentHorizontalPadding = 20.dp

    val title = if (!chapter.chapter.isNullOrEmpty() && !chapter.title.isNullOrEmpty()) {
        stringResource(id = R.string.chapter_name_title, chapter.chapter, chapter.title)
    } else if (!chapter.chapter.isNullOrEmpty()) {
        stringResource(id = R.string.chapter_name, chapter.chapter)
    } else if (!chapter.volume.isNullOrEmpty()) {
        stringResource(id = R.string.chapter_name_volume, chapter.volume)
    } else {
        ""
    }

    Column(modifier = Modifier
        .padding(horizontal = contentHorizontalPadding)
        .height(CHAPTER_HEIGHT)
        .clickable { onChapterClick(chapter) }) {
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = title,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = chapter.groupNameList?.getOrNull(0) ?: "",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = typography.bodySmall,
                modifier = Modifier.weight(weight = 1f)
            )
            Text(text = chapter.lang, style = typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Divider()
    }
}