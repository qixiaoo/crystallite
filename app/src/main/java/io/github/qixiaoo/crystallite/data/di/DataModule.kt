package io.github.qixiaoo.crystallite.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.qixiaoo.crystallite.data.network.ComickNetwork
import io.github.qixiaoo.crystallite.data.network.ComickNetworkDataSource
import io.github.qixiaoo.crystallite.data.repository.ComickRepository
import io.github.qixiaoo.crystallite.data.repository.LocalUserPreferencesRepository
import io.github.qixiaoo.crystallite.data.repository.NetworkComickRepository
import io.github.qixiaoo.crystallite.data.repository.UserPreferencesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindComickRepository(networkComickRepository: NetworkComickRepository): ComickRepository

    @Binds
    @Singleton
    fun bindComickNetworkDataSource(comickNetwork: ComickNetwork): ComickNetworkDataSource

    @Binds
    fun bindUserPreferencesRepository(userPreferencesRepository: LocalUserPreferencesRepository): UserPreferencesRepository
}
