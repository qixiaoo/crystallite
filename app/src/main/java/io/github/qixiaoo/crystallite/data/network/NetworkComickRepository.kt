package io.github.qixiaoo.crystallite.data.network

import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.model.TopComics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class NetworkComickRepository @Inject constructor(private val network: ComickNetworkDataSource) :
    ComickRepository {

    override fun top(gender: Gender, isMature: Boolean): Flow<TopComics> {
        return flow {
            val response = network.top(gender, isMature)
            emit(response)
        }
    }
}
