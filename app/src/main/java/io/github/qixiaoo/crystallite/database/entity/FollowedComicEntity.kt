package io.github.qixiaoo.crystallite.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import io.github.qixiaoo.crystallite.data.model.FollowedComic
import io.github.qixiaoo.crystallite.data.model.FollowedComicReadingChapter
import io.github.qixiaoo.crystallite.data.model.MdCover

@Entity(tableName = "followed_comic", indices = [Index(value = ["hid", "slug", "title"])])
data class FollowedComicEntity(
    @PrimaryKey(autoGenerate = true)
    val entityId: Long,

    @ColumnInfo(defaultValue = "")
    val hid: String,

    @ColumnInfo(defaultValue = "")
    val slug: String,

    @ColumnInfo(defaultValue = "")
    val title: String,

    @ColumnInfo(name = "cover_width")
    val coverWidth: Int,

    @ColumnInfo(name = "cover_height")
    val coverHeight: Int,

    @ColumnInfo(name = "cover_b2_key")
    val coverB2Key: String,

    @ColumnInfo(name = "reading_chapter_hid")
    val readingChapterHid: String,

    @ColumnInfo(name = "reading_chapter_number")
    val readingChapterNumber: String,

    @ColumnInfo(name = "reading_chapter_curr_page")
    val readingChapterCurrPage: Int,

    @ColumnInfo(name = "reading_chapter_total_page")
    val readingChapterTotalPage: Int,

    @ColumnInfo(name = "next_chapter_hid")
    val nextChapterHid: String,
)

fun FollowedComicEntity.toModel(): FollowedComic {
    val readingChapter = if (readingChapterHid.isEmpty()) {
        null
    } else {
        FollowedComicReadingChapter(
            readingChapterHid = readingChapterHid,
            readingChapterNumber = readingChapterNumber,
            readingChapterCurrPage = readingChapterCurrPage,
            readingChapterTotalPage = readingChapterTotalPage,
            nextChapterHid = nextChapterHid,
        )
    }

    return FollowedComic(
        entityId = entityId,
        hid = hid,
        slug = slug,
        title = title,
        mdCover = MdCover(w = coverWidth, h = coverHeight, b2key = coverB2Key),
        readingChapter = readingChapter
    )
}

fun FollowedComic.toEntity(): FollowedComicEntity {
    return FollowedComicEntity(
        entityId = entityId,
        hid = hid,
        slug = slug,
        title = title,

        coverWidth = mdCover.w,
        coverHeight = mdCover.h,
        coverB2Key = mdCover.b2key,

        readingChapterHid = readingChapter?.readingChapterHid ?: "",
        readingChapterNumber = readingChapter?.readingChapterNumber ?: "0",
        readingChapterCurrPage = readingChapter?.readingChapterCurrPage ?: 0,
        readingChapterTotalPage = readingChapter?.readingChapterTotalPage ?: 0,
        nextChapterHid = readingChapter?.nextChapterHid ?: ""
    )
}