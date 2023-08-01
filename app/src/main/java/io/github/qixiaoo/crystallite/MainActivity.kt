package io.github.qixiaoo.crystallite

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import io.github.qixiaoo.crystallite.logic.network.RetrofitServiceCreator

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitServiceCreator.initialize(applicationContext as Application)

        setContent {
            Main()
        }
    }
}
