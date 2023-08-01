package io.github.qixiaoo.crystallite.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.ui.components.RecentComics


@Composable
internal fun Home(homeViewModel: HomeViewModel = viewModel()) {
    val topComics by homeViewModel.topComics.collectAsStateWithLifecycle()

    val padding = 20.dp
    val spaceBetween = 30.dp

    Column(verticalArrangement = Arrangement.spacedBy(spaceBetween)) {
        RecentComics(
            comics = topComics.trending,
            title = stringResource(id = R.string.most_viewed),
            padding = padding,
            modifier = Modifier.padding(top = spaceBetween)
        )

        RecentComics(
            comics = topComics.topFollowNewComics,
            title = stringResource(id = R.string.popular_new_comics),
            padding = padding
        )
    }
}
