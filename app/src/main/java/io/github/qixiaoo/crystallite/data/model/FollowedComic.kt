package io.github.qixiaoo.crystallite.data.model

data class FollowedComic(
    val entityId: Long,
    val hid: String,
    val slug: String,
    val title: String,
    val mdCover: MdCover,
    val readingChapter: FollowedComicReadingChapter?
)


data class FollowedComicReadingChapter(
    val readingChapterHid: String,
    val readingChapterNumber: String,
    val readingChapterCurrPage: Int,
    val readingChapterTotalPage: Int,
    val nextChapterHid: String,
)
