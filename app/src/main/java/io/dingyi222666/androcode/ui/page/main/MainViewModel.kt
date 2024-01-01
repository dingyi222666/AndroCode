package io.dingyi222666.androcode.ui.page.main

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import io.dingyi222666.androcode.api.Androcode
import io.dingyi222666.androcode.api.navigationBar
import io.dingyi222666.androcode.api.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _isExpandedMenu = MutableStateFlow(false)

    val isExpandedMenu = _isExpandedMenu.asStateFlow()

    private val _drawerShouldBeOpened = MutableStateFlow(false)
    val drawerShouldBeOpened = _drawerShouldBeOpened.asStateFlow()

    val navigationBarItemList = Androcode.ui.navigationBar.navigationBarItemFlow

    val _snackbarHostState = MutableStateFlow(SnackbarHostState())
    val snackbarHostState = _snackbarHostState.asStateFlow()

    fun openDrawer() {
        _drawerShouldBeOpened.value = true
    }

    fun toggleDrawer() {
        _drawerShouldBeOpened.value = _drawerShouldBeOpened.value.not()
    }

    fun toggleMenu() {
        _isExpandedMenu.value = _isExpandedMenu.value.not()
    }

    fun resetOpenDrawerAction() {
        _drawerShouldBeOpened.value = false
    }
}