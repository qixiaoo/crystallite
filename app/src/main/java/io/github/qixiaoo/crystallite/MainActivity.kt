package io.github.qixiaoo.crystallite

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import io.github.qixiaoo.crystallite.data.network.RetrofitServiceCreator

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitServiceCreator.initialize(applicationContext as Application)

        setContent {
            Main()
        }
    }
}
