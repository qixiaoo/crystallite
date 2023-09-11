package io.github.qixiaoo.crystallite.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    keyword: String = "",
    onKeywordChange: (String) -> Unit = {},
    onClickSearch: () -> Unit = {},
    onClickBack: () -> Unit = {},
) {
    var active by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .then(modifier),
        query = keyword,
        onQueryChange = { onKeywordChange(it) },
        onSearch = {
            active = false
            onClickSearch()
        },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text(text = stringResource(R.string.search)) },
        leadingIcon = {
            if (!active) {
                Icon(Icons.Default.Search, contentDescription = "search")
            } else {
                IconButton(onClick = {
                    active = false
                    onClickBack()
                }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "back",
                    )
                }
            }
        },
        trailingIcon = {
            if (keyword.isNotEmpty()) {
                IconButton(onClick = { onKeywordChange("") }) {
                    Icon(
                        Icons.Default.Cancel,
                        contentDescription = "cancel",
                    )
                }
            }
        },
    ) {
        // TODO: Search history
    }
}


@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    CrystalliteTheme {
        AppBar()
    }
}