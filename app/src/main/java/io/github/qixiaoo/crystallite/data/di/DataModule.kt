package io.github.qixiaoo.crystallite.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.qixiaoo.crystallite.data.network.ComickNetwork
import io.github.qixiaoo.crystallite.data.network.ComickNetworkDataSource
import io.github.qixiaoo.crystallite.data.network.ComickRepository
import io.github.qixiaoo.crystallite.data.network.NetworkComickRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindComickRepository(networkComickRepository: NetworkComickRepository): ComickRepository

    @Binds
    fun bindComickNetworkDataSource(comickNetwork: ComickNetwork): ComickNetworkDataSource
}
