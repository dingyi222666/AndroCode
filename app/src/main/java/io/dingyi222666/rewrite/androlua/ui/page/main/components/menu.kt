package io.dingyi222666.rewrite.androlua.ui.page.main.components

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


@Composable
fun MainMenu(isExpanded: Boolean, onDismissRequest: () -> Unit) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.width(160.dp)
    ) {

        DropdownMenuItem(text = { Text("setttings") },
            onClick = { /* Handle refresh! */ },
            leadingIcon = {
                Icon(
                    Icons.TwoTone.Settings, contentDescription = null
                )
            })
        DropdownMenuItem(text = { Text("about") },
            onClick = { /* Handle settings! */ },
            leadingIcon = {
                Icon(
                    Icons.TwoTone.Info, contentDescription = null
                )
            })
    }
}
