package io.github.qixiaoo.crystallite.data.network

import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.model.TopComics
import kotlinx.coroutines.flow.Flow

interface ComickRepository {
    fun top(gender: Gender, isMature: Boolean = false): Flow<TopComics>
}