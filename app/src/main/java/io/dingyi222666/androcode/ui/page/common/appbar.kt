package io.dingyi222666.androcode.ui.page.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.dingyi222666.androcode.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHubAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = title,
        actions = actions,
        navigationIcon = navigationIcon,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ChatHubAppBarPreview() {
    ChatHubAppBar(
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(Icons.TwoTone.ArrowBack, contentDescription = null)
            }
        },
        title = { Text(text = stringResource(R.string.app_name)) },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.TwoTone.Add, contentDescription = null)
            }

            IconButton(onClick = {}) {
                Icon(Icons.TwoTone.MoreVert, contentDescription = null)
            }
        }
    )
}
