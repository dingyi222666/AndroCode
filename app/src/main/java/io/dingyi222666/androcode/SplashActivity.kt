package io.dingyi222666.androcode

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import io.dingyi222666.androcode.api.init.InitStatus
import io.dingyi222666.androcode.api.init.init
import io.dingyi222666.androcode.ui.page.splash.SplashPage
import io.dingyi222666.androcode.ui.page.splash.SplashViewModel
import io.dingyi222666.androcode.ui.resource.theme.AndroCodeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
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

        MainApplication.instance.initIDEContext()

        initSplash()
    }

    private fun initSplash() {
        val sharedFlow = MutableSharedFlow<InitStatus>()


        lifecycleScope.launch(Dispatchers.Main) {
            sharedFlow.collectLatest {
                val throwable = it.error
                if (throwable != null) {
                    throw throwable
                }
                viewModel.emitStatus(it.formattedMessage)
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            MainApplication.instance.androCode
                .init.init(this@SplashActivity, sharedFlow)

            startActivity(
                Intent(this@SplashActivity, MainActivity::class.java)
            )
            finish()
        }
    }
}




