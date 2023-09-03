package io.github.qixiaoo.crystallite.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.qixiaoo.crystallite.common.Result
import io.github.qixiaoo.crystallite.common.asResult
import io.github.qixiaoo.crystallite.data.model.SearchResultComic
import io.github.qixiaoo.crystallite.data.model.SearchResultType
import io.github.qixiaoo.crystallite.data.network.ComickRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(val comickRepository: ComickRepository) : ViewModel() {
    var type = MutableStateFlow(SearchResultType.COMIC)

    var keyword = MutableStateFlow("")

    val comicList = comicListUiState(comickRepository, type, keyword)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ComicListUiState.Loading
        )
}

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
private fun comicListUiState(
    comickRepository: ComickRepository,
    type: MutableStateFlow<SearchResultType>,
    keyword: MutableStateFlow<String>,
): Flow<ComicListUiState> {
    return keyword.debounce(500)
        .combine(type.filter { it == SearchResultType.COMIC }, ::Pair)
        .distinctUntilChanged()
        .flatMapLatest {
            val currentKeyword = it.first

            if (currentKeyword.isEmpty()) {
                return@flatMapLatest flowOf(emptyList())
            }

            Log.v(::comicListUiState.name, "search comic list: $currentKeyword")
            comickRepository.searchComicByKeyword(keyword = currentKeyword)
        }
        .asResult()
        .map { result ->
            when (result) {
                Result.Loading -> ComicListUiState.Loading
                is Result.Error -> ComicListUiState.Error(result.exception?.message)
                is Result.Success -> ComicListUiState.Success(result.data)
            }
        }
}

sealed interface ComicListUiState {
    object Loading : ComicListUiState
    data class Error(val message: String? = null) : ComicListUiState
    data class Success(val comicList: List<SearchResultComic>) : ComicListUiState
}