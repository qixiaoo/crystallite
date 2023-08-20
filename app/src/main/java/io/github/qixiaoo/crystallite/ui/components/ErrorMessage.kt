package io.github.qixiaoo.crystallite.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme


@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    val typography = MaterialTheme.typography

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
    ) {
        Icon(imageVector = Icons.Filled.Error, contentDescription = "error icon")
        Spacer(modifier = Modifier.height(5.dp))
        SelectionContainer {
            Text(
                text = message ?: stringResource(id = R.string.default_error_message),
                style = typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorMessagePreview() {
    CrystalliteTheme {
        ErrorMessage()
    }
}
