package io.github.qixiaoo.crystallite.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.qixiaoo.crystallite.common.GRID_ROW_ITEM_COUNT
import io.github.qixiaoo.crystallite.common.Result
import io.github.qixiaoo.crystallite.common.asResult
import io.github.qixiaoo.crystallite.data.model.Comic
import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.data.model.TopComics
import io.github.qixiaoo.crystallite.data.network.ComickRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val comickRepository: ComickRepository) :
    ViewModel() {

    val topComicsUiState: StateFlow<TopComicsUiState> = topComicsUiState(comickRepository).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TopComicsUiState.Loading
    )

    val news: StateFlow<List<List<Comic>>> =
        topComicsUiState.filter { it is TopComicsUiState.Success }
            .map { (it as TopComicsUiState.Success).topComics }
            .map { it.news.chunked(GRID_ROW_ITEM_COUNT) }.stateIn(
                scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList()
            )

    val completions: StateFlow<List<List<Comic>>> =
        topComicsUiState.filter { it is TopComicsUiState.Success }
            .map { (it as TopComicsUiState.Success).topComics }
            .map { it.completions.chunked(GRID_ROW_ITEM_COUNT) }.stateIn(
                scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList()
            )
}


private fun topComicsUiState(comickRepository: ComickRepository): Flow<TopComicsUiState> {
    Log.v(::topComicsUiState.name, "fetch top comics")
    return comickRepository.top(Gender.MALE).asResult().map { result ->
        when (result) {
            is Result.Error -> TopComicsUiState.Error(message = result.exception?.message)
            Result.Loading -> TopComicsUiState.Loading
            is Result.Success -> TopComicsUiState.Success(topComics = result.data)
        }
    }
}


sealed interface TopComicsUiState {
    object Loading : TopComicsUiState
    data class Error(val message: String? = null) : TopComicsUiState
    data class Success(val topComics: TopComics) : TopComicsUiState
}