package com.muhdila.accurateuserapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.muhdila.accurateuserapp.core.presentation.AccurateUserAppTheme
import com.muhdila.accurateuserapp.user.presentation.ui.UserScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AccurateUserAppTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                UserScreen(
                    windowWidthSizeClass = windowSizeClass.widthSizeClass
                )
            }
        }
    }
}
