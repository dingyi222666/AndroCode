package io.dingyi222666.rewrite.androlua.ui.page.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import io.dingyi222666.rewrite.androlua.ui.page.common.DrawerValue
import io.dingyi222666.rewrite.androlua.ui.page.common.rememberDrawerState
import io.dingyi222666.rewrite.androlua.ui.page.main.components.MainAppBar
import io.dingyi222666.rewrite.androlua.ui.page.main.components.drawer.MainDrawer
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {

    val viewModel = viewModel<MainViewModel>()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerOpen by viewModel.drawerShouldBeOpened
        .collectAsStateWithLifecycle()

    if (drawerOpen) {
        // Open drawer and reset state in VM.
        LaunchedEffect(Unit) {
            // wrap in try-finally to handle interruption whiles opening drawer
            try {
                drawerState.open()
            } finally {
                viewModel.resetOpenDrawerAction()
            }
        }
    }

    // Intercepts back navigation when the drawer is open
    val scope = rememberCoroutineScope()
    if (drawerState.isOpen) {
        BackHandler {
            scope.launch {
                drawerState.close()
            }
        }
    }


    val snackbarHostState = remember {
        viewModel.snackbarHostState
    }

    MainDrawer(
        drawerState = drawerState,
    ) {
        MainPageContent(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            viewModel = viewModel
        )
    }

}

@Composable
fun MainPageContent(
    snackbarHost: @Composable () -> Unit = {},
    viewModel: MainViewModel
) {
    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            MainAppBar(viewModel)
        },
    ) { paddingValues ->
        // using paddingValues

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), color = MaterialTheme.colorScheme.background
        ) {
            Text("Android")
        }

    }
}
