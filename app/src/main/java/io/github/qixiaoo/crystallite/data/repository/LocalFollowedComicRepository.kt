package io.github.qixiaoo.crystallite.data.repository

import io.github.qixiaoo.crystallite.data.model.FollowedComic
import io.github.qixiaoo.crystallite.database.dao.FollowedComicDao
import io.github.qixiaoo.crystallite.database.entity.toEntity
import io.github.qixiaoo.crystallite.database.entity.toModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalFollowedComicRepository @Inject constructor(private val followedComicDao: FollowedComicDao) :
    FollowedComicRepository {

    override fun getFollowedComics() =
        followedComicDao.getFollowedComics().map { it.map { entity -> entity.toModel() } }

    override suspend fun followComics(comics: List<FollowedComic>) =
        followedComicDao.insertOrIgnoreFollowedComics(comics = comics.map { it.toEntity() })

    override suspend fun unfollowComics(comicHids: List<String>) =
        followedComicDao.deleteFollowedComics(comicHids)
}