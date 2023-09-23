package io.github.qixiaoo.crystallite.ui.screens.me

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import io.github.qixiaoo.crystallite.data.model.Gender
import io.github.qixiaoo.crystallite.ui.common.toResourceId
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme
import kotlinx.coroutines.launch


@Composable
internal fun Me(meViewModel: MeViewModel = hiltViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val volumeKeysNavigation = meViewModel.volumeKeysNavigation.collectAsStateWithLifecycle()
    val gender = meViewModel.gender.collectAsStateWithLifecycle()

    MeContent(
        gender = gender.value,
        volumeKeysNavigation = volumeKeysNavigation.value,
        onVolumeKeysNavigationChange = { enabled ->
            coroutineScope.launch {
                meViewModel.setVolumeKeysNavigation(
                    enabled
                )
            }
        },
        onGenderChange = {
            coroutineScope.launch {
                meViewModel.setGender(it)
            }
        }
    )
}


@Composable
private fun MeContent(
    gender: Gender,
    onGenderChange: (Gender) -> Unit,
    volumeKeysNavigation: Boolean,
    onVolumeKeysNavigationChange: (Boolean) -> Unit,
) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    val openGenderFiltersDialog = remember { mutableStateOf(false) }

    val horizontalPadding = 20.dp
    val titleTopPadding = 30.dp + 8.dp
    val titleBottomPadding = 15.dp
    val columnSpacer = 15.dp

    Column(
        verticalArrangement = Arrangement.spacedBy(columnSpacer),
        modifier = Modifier.fillMaxSize()
    ) {
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
            ListItem(
                headlineContent = {
                    Text(stringResource(R.string.gender_filter))
                },
                supportingContent = {
                    Text(stringResource(gender.toResourceId()))
                },
                modifier = Modifier.clickable { openGenderFiltersDialog.value = true }
            )
        }

        ElevatedCard(modifier = Modifier.padding(horizontal = horizontalPadding)) {
            Column {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.volume_keys_navigation)) },
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

    GenderFiltersDialog(
        gender = gender,
        open = openGenderFiltersDialog.value,
        onClose = { openGenderFiltersDialog.value = false },
        onGenderChange = onGenderChange
    )
}


@Preview(showBackground = true)
@Composable
fun MeContentPreview() {
    CrystalliteTheme {
        var volumeKeysNavigation by remember { mutableStateOf(false) }

        MeContent(
            gender = Gender.UNKNOWN,
            volumeKeysNavigation = volumeKeysNavigation,
            onVolumeKeysNavigationChange = { volumeKeysNavigation = it },
            onGenderChange = {}
        )
    }
}


@Composable
private fun GenderFiltersDialog(
    gender: Gender,
    onGenderChange: (Gender) -> Unit,
    open: Boolean,
    onClose: () -> Unit,
) {
    val genderFilters = listOf(Gender.MALE, Gender.FEMALE, Gender.UNKNOWN)

    if (open) {
        AlertDialog(
            onDismissRequest = onClose,
            title = { Text(text = stringResource(R.string.gender)) },
            text = {
                Column {
                    genderFilters.forEach {
                        ListItem(
                            leadingContent = {
                                RadioButton(
                                    selected = it == gender,
                                    onClick = null,
                                )
                            },
                            headlineContent = { Text(stringResource(it.toResourceId())) },
                            modifier = Modifier.clickable {
                                onGenderChange(it)
                                onClose()
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onClose) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GenderFiltersDialogPreview() {
    CrystalliteTheme {
        GenderFiltersDialog(
            gender = Gender.UNKNOWN,
            open = true,
            onGenderChange = {},
            onClose = {}
        )
    }
}
