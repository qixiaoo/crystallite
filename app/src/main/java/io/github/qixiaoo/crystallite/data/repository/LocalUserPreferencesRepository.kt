package io.github.qixiaoo.crystallite.data.repository

import io.github.qixiaoo.crystallite.data.model.ReadingMode
import io.github.qixiaoo.crystallite.data.model.UserPreferences
import io.github.qixiaoo.crystallite.datastore.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalUserPreferencesRepository @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : UserPreferencesRepository {
    override val userPreferences: Flow<UserPreferences> = userPreferencesDataSource.userPreferences

    override suspend fun setReadingMode(mode: ReadingMode) =
        userPreferencesDataSource.setReadingMode(mode)

    override suspend fun setVolumeKeysNavigation(enabled: Boolean) =
        userPreferencesDataSource.setVolumeKeysNavigation(enabled)
}
