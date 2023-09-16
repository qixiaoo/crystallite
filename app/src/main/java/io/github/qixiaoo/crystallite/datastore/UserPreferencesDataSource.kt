package io.github.qixiaoo.crystallite.datastore

import androidx.datastore.core.DataStore
import io.github.qixiaoo.crystallite.data.model.ReadingMode
import io.github.qixiaoo.crystallite.data.model.UserPreferences
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(private val userPreferencesDataStore: DataStore<UserPreferencesProto>) {
    val userPreferences = userPreferencesDataStore.data.map {
        UserPreferences(
            readingMode = it.readingMode.toModel()
        )
    }

    suspend fun setReadingMode(mode: ReadingMode) {
        userPreferencesDataStore.updateData { currentUserPreferences ->
            val modeProto = mode.toProto()
            currentUserPreferences.toBuilder().setReadingMode(modeProto).build()
        }
    }
}
