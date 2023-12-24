package io.dingyi222666.rewrite.androlua

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import io.dingyi222666.rewrite.androlua.ui.page.NavGraph
import io.dingyi222666.rewrite.androlua.ui.resource.LocalWindowSizeClass
import io.dingyi222666.rewrite.androlua.ui.resource.theme.ReWriteAndroLuaTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            val sizeClass = calculateWindowSizeClass(this)

            CompositionLocalProvider(
                LocalWindowSizeClass provides sizeClass
            ) {
                ReWriteAndroLuaTheme {
                    NavGraph()
                }
            }
        }
    }
}

