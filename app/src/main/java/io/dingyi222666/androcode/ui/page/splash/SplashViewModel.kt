package io.dingyi222666.androcode.ui.page.splash

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SplashViewModel : ViewModel() {

    private val _statusText = MutableStateFlow("Loading...")

    private val _splashStatus = MutableStateFlow(false)

    val statusText = _statusText.asStateFlow()

    val splashStatus = _splashStatus.asStateFlow()

    fun emitStatus(status: String) {
        _statusText.value = status
    }

    fun finishSplash() {
        _splashStatus.value = true
    }

}