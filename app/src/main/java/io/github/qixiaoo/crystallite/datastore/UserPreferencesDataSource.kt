package io.github.qixiaoo.crystallite.datastore

import androidx.datastore.core.DataStore
import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.model.ReadingMode
import io.github.qixiaoo.crystallite.data.model.UserPreferences
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(private val userPreferencesDataStore: DataStore<UserPreferencesProto>) {
    val userPreferences = userPreferencesDataStore.data.map {
        UserPreferences(
            readingMode = it.readingMode.toModel(),
            volumeKeysNavigation = it.volumeKeysNavigation,
            gender = it.gender.toModel()
        )
    }

    suspend fun setReadingMode(mode: ReadingMode) {
        userPreferencesDataStore.updateData { currentUserPreferences ->
            val modeProto = mode.toProto()
            currentUserPreferences.toBuilder().setReadingMode(modeProto).build()
        }
    }

    suspend fun setVolumeKeysNavigation(enabled: Boolean) {
        userPreferencesDataStore.updateData { currentUserPreferences ->
            currentUserPreferences.toBuilder().setVolumeKeysNavigation(enabled).build()
        }
    }

    suspend fun setGender(gender: Gender) {
        userPreferencesDataStore.updateData { currentUserPreferences ->
            currentUserPreferences.toBuilder().setGender(gender.toProto()).build()
        }
    }
}
