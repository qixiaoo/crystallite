package io.github.qixiaoo.crystallite.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import io.github.qixiaoo.crystallite.database.entity.FollowedComicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowedComicDao {
    @Query(
        value = """
            SELECT * FROM followed_comic
            WHERE hid = :comicHid
        """
    )
    fun getFollowedComic(comicHid: String): Flow<FollowedComicEntity?>

    @Query(value = "SELECT * FROM followed_comic")
    fun getFollowedComics(): Flow<List<FollowedComicEntity>>

    @Query(
        value = """
            DELETE FROM followed_comic
            WHERE hid in (:comicHids)
        """,
    )
    suspend fun deleteFollowedComics(comicHids: List<String>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreFollowedComics(comics: List<FollowedComicEntity>): List<Long>

    @Upsert
    suspend fun upsertFollowedComics(comics: List<FollowedComicEntity>)
}