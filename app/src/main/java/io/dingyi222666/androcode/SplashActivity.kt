package io.dingyi222666.androcode

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import io.dingyi222666.androcode.ui.page.splash.SplashPage
import io.dingyi222666.androcode.ui.page.splash.SplashViewModel
import io.dingyi222666.androcode.ui.resource.theme.AndroCodeTheme


class SplashActivity : ComponentActivity() {

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroCodeTheme {
                SplashPage()
            }
        }
    }


}


