package io.dingyi222666.androcode.ui.page.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Menu
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.dingyi222666.androcode.R
import io.dingyi222666.androcode.ui.page.main.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    viewModel: MainViewModel
) {

    val showMenuState by viewModel.isExpandedMenu.collectAsStateWithLifecycle()

    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.TwoTone.PlayArrow, contentDescription = null)
            }

            IconButton(onClick = {
                viewModel.toggleMenu()
            }) {
                Icon(Icons.TwoTone.MoreVert, contentDescription = null)
            }

            MainMenu(isExpanded = showMenuState) {
                viewModel.toggleMenu()
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                viewModel.toggleDrawer()
            }) {
                Icon(Icons.TwoTone.Menu, contentDescription = null)
            }
        }
    )
}
