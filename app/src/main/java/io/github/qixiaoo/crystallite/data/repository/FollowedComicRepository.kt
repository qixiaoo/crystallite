package io.github.qixiaoo.crystallite.data.repository

import io.github.qixiaoo.crystallite.data.model.FollowedComic
import kotlinx.coroutines.flow.Flow

interface FollowedComicRepository {
    fun getFollowedComic(comicHid: String): Flow<FollowedComic?>
    fun getFollowedComics(): Flow<List<FollowedComic>>
    suspend fun followComics(comics: List<FollowedComic>): List<Long>
    suspend fun unfollowComics(comicHids: List<String>)
    suspend fun updateFollowedComic(comic: FollowedComic)
}