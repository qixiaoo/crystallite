package io.github.qixiaoo.crystallite.data.repository

import io.github.qixiaoo.crystallite.data.model.ReadingMode
import io.github.qixiaoo.crystallite.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferences: Flow<UserPreferences>

    suspend fun setReadingMode(mode: ReadingMode)

    suspend fun setVolumeKeysNavigation(enabled: Boolean)
}