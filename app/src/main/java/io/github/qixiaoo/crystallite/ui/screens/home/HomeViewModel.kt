package io.github.qixiaoo.crystallite.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.qixiaoo.crystallite.logic.model.ComicListOrderedByPeriod
import io.github.qixiaoo.crystallite.logic.model.TopComics
import io.github.qixiaoo.crystallite.logic.network.ComickRepository
import io.github.qixiaoo.crystallite.logic.model.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


val EMPTY_ORDERED_COMIC_LIST = ComicListOrderedByPeriod(
    week = emptyList(),
    month = emptyList(),
    quarter = emptyList(),
    halfYear = null,
    threeQuarters = null,
    year = null,
    twoYears = null
)

val EMPTY_TOP_COMICS = TopComics(
    rank = emptyList(),
    recentRank = emptyList(),
    trending = EMPTY_ORDERED_COMIC_LIST,
    news = emptyList(),
    extendedNews = emptyList(),
    completions = emptyList(),
    topFollowNewComics = EMPTY_ORDERED_COMIC_LIST,
    topFollowComics = EMPTY_ORDERED_COMIC_LIST
)


class HomeViewModel : ViewModel() {
    private val topComicsMut = MutableStateFlow(EMPTY_TOP_COMICS)

    val topComics: StateFlow<TopComics> = topComicsMut.asStateFlow()

    init {
        viewModelScope.launch {
            Log.v("HomeViewModel", "init: fetch top comics")
            ComickRepository.top(Gender.MALE).collect { value -> topComicsMut.value = value }
        }
    }
}