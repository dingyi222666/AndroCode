package io.dingyi222666.androcode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import io.dingyi222666.androcode.ui.built.navigation
import io.dingyi222666.androcode.ui.page.NavGraph
import io.dingyi222666.androcode.ui.resource.LocalWindowSizeClass
import io.dingyi222666.androcode.ui.resource.theme.AndroCodeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainApplication.instance.initIDEContext()
        // TODO: move to built plugin
        navigation(this)

        setContent {
            val sizeClass = calculateWindowSizeClass(this)

            CompositionLocalProvider(
                LocalWindowSizeClass provides sizeClass
            ) {
                AndroCodeTheme {
                    NavGraph()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MainApplication.instance.androCode.dispose()
    }
}

