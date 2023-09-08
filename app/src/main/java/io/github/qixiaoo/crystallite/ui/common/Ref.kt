package io.github.qixiaoo.crystallite.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class Ref<T>(var value: T)


@Composable
fun <T> rememberRef(initialValue: T): Ref<T> {
    return remember {
        Ref(initialValue)
    }
}
