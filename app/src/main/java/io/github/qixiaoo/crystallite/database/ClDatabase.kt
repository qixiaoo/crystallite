package io.github.qixiaoo.crystallite.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.qixiaoo.crystallite.database.dao.FollowedComicDao
import io.github.qixiaoo.crystallite.database.entity.FollowedComicEntity

@Database(
    entities = [FollowedComicEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class ClDatabase : RoomDatabase() {
    abstract fun followedComicDao(): FollowedComicDao
}