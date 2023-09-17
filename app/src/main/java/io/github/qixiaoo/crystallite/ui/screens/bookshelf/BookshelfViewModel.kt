package io.github.qixiaoo.crystallite.ui.screens.bookshelf

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.qixiaoo.crystallite.common.Result
import io.github.qixiaoo.crystallite.common.asResult
import io.github.qixiaoo.crystallite.data.model.FollowedComic
import io.github.qixiaoo.crystallite.data.repository.FollowedComicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class BookshelfViewModel @Inject constructor(
    followedComicRepository: FollowedComicRepository,
) : ViewModel() {

    val bookshelfUiState: StateFlow<BookshelfUiState> = bookshelfUiState(
        followedComicRepository = followedComicRepository
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BookshelfUiState.Loading
    )
}


private fun bookshelfUiState(
    followedComicRepository: FollowedComicRepository,
): Flow<BookshelfUiState> {
    Log.v(::bookshelfUiState.name, "query followed comics from database")
    return followedComicRepository.getFollowedComics().asResult().map { result ->
        when (result) {
            is Result.Error -> BookshelfUiState.Error(message = result.exception?.message)
            Result.Loading -> BookshelfUiState.Loading
            is Result.Success -> BookshelfUiState.Success(followedComics = result.data)
        }
    }
}


sealed interface BookshelfUiState {
    object Loading : BookshelfUiState
    data class Error(val message: String? = null) : BookshelfUiState
    data class Success(val followedComics: List<FollowedComic>) : BookshelfUiState
}