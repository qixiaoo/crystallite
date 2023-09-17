package io.github.qixiaoo.crystallite.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.qixiaoo.crystallite.database.ClDatabase
import io.github.qixiaoo.crystallite.database.dao.FollowedComicDao

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesFollowedComicDao(
        database: ClDatabase,
    ): FollowedComicDao = database.followedComicDao()
}
