package io.github.qixiaoo.crystallite.ui.screens.me

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme
import kotlinx.coroutines.launch


@Composable
internal fun Me(meViewModel: MeViewModel = hiltViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val volumeKeysNavigation = meViewModel.volumeKeysNavigation.collectAsStateWithLifecycle()

    MeContent(
        volumeKeysNavigation = volumeKeysNavigation.value,
        onVolumeKeysNavigationChange = { enabled ->
            coroutineScope.launch {
                meViewModel.setVolumeKeysNavigation(
                    enabled
                )
            }
        }
    )
}


@Composable
private fun MeContent(
    volumeKeysNavigation: Boolean,
    onVolumeKeysNavigationChange: (Boolean) -> Unit,
) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    val horizontalPadding = 20.dp
    val titleTopPadding = 30.dp + 8.dp
    val titleBottomPadding = 15.dp

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.settings),
            color = colorScheme.onBackground,
            style = typography.titleLarge,
            modifier = Modifier.padding(
                start = horizontalPadding,
                end = horizontalPadding,
                top = titleTopPadding,
                bottom = titleBottomPadding
            )
        )

        ElevatedCard(modifier = Modifier.padding(horizontal = horizontalPadding)) {
            Column {
                ListItem(
                    headlineContent = {
                        Text(stringResource(R.string.volume_keys_navigation))
                    },
                    trailingContent = {
                        Switch(
                            checked = volumeKeysNavigation,
                            onCheckedChange = onVolumeKeysNavigationChange
                        )
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MeContentPreview() {
    CrystalliteTheme {
        var volumeKeysNavigation by remember { mutableStateOf(false) }

        MeContent(
            volumeKeysNavigation = volumeKeysNavigation,
            onVolumeKeysNavigationChange = { volumeKeysNavigation = it }
        )
    }
}
