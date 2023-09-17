package io.github.qixiaoo.crystallite.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import io.github.qixiaoo.crystallite.data.model.FollowedComic

@Entity(tableName = "followed_comic", indices = [Index(value = ["hid", "title"])])
data class FollowedComicEntity(
    @PrimaryKey(autoGenerate = true)
    val entityId: Long,

    @ColumnInfo(defaultValue = "")
    val hid: String,

    @ColumnInfo(defaultValue = "")
    val slug: String,

    @ColumnInfo(defaultValue = "")
    val title: String,
)

fun FollowedComicEntity.toModel(): FollowedComic {
    return FollowedComic(
        entityId = entityId,
        hid = hid,
        slug = slug,
        title = title
    )
}

fun FollowedComic.toEntity(): FollowedComicEntity {
    return FollowedComicEntity(
        entityId = entityId,
        hid = hid,
        slug = slug,
        title = title
    )
}