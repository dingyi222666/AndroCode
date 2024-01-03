package io.dingyi222666.androcode.ui.page.splash

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SplashViewModel : ViewModel() {

    private val _statusText = MutableStateFlow("Loading...")


    val statusText = _statusText.asStateFlow()


    fun emitStatus(status: String) {
        _statusText.value = status
    }


}