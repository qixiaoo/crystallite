package io.github.qixiaoo.crystallite.logic.network

import io.github.qixiaoo.crystallite.logic.model.Gender
import io.github.qixiaoo.crystallite.logic.model.TopComics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


object ComickRepository {
    fun top(gender: Gender, isMature: Boolean = false): Flow<TopComics> {
        return flow {
            val response = ComickNetwork.top(gender, isMature)
            emit(response)
        }
    }
}

