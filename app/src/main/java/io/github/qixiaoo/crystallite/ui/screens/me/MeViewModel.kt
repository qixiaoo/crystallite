package io.github.qixiaoo.crystallite.ui.screens.me

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class MeViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    val volumeKeysNavigation =
        userPreferencesRepository.userPreferences.map { it.volumeKeysNavigation }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val gender = userPreferencesRepository.userPreferences.map { it.gender }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Gender.UNKNOWN
    )

    suspend fun setVolumeKeysNavigation(enabled: Boolean) {
        userPreferencesRepository.setVolumeKeysNavigation(enabled)
    }

    suspend fun setGender(gender: Gender) {
        userPreferencesRepository.setGender(gender)
    }
}
