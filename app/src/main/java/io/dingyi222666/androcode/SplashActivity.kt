package io.dingyi222666.androcode

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import io.dingyi222666.androcode.ui.page.splash.SplashPage
import io.dingyi222666.androcode.ui.page.splash.SplashViewModel
import io.dingyi222666.androcode.ui.resource.theme.AndroCodeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.job
import kotlinx.coroutines.launch


class SplashActivity : ComponentActivity() {


    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Default)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroCodeTheme {
                SplashPage()
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.splashStatus
                .collect {
                    if (!it) {
                        return@collect
                    }

                    startActivity(
                        Intent(this@SplashActivity, MainActivity::class.java)
                    )
                    finish()

                    return@collect cancel()
                }

        }
    }
}




