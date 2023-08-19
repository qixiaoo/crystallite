package io.github.qixiaoo.crystallite.data.network

import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.model.TopComics

/**
 * Interface representing network calls to the Comick backend
 */
interface ComickNetworkDataSource {
    suspend fun top(gender: Gender, isMature: Boolean): TopComics
}