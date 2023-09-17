package io.github.qixiaoo.crystallite.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.qixiaoo.crystallite.database.ClDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesClDatabase(
        @ApplicationContext context: Context,
    ): ClDatabase = Room.databaseBuilder(
        context,
        ClDatabase::class.java,
        "cl-database",
    ).build()
}
