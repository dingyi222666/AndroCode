package io.dingyi222666.rewrite.androlua.ui.page.main.components.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Build
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.material.icons.twotone.MyLocation
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.dingyi222666.rewrite.androlua.ui.page.main.MainViewModel


@Composable
@Preview
fun NavigationBar() {
    val viewModel = viewModel<MainViewModel>()

    val navigationBarItems by viewModel.navigationBarItemList
        .collectAsStateWithLifecycle()

    var selectedTag by rememberSaveable {
        mutableStateOf(navigationBarItems[0].id)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .width(64.dp)
                .fillMaxHeight()
                .clip(
                    RoundedCornerShape(0.dp, 16.dp, 16.dp, 0.dp)
                )
                .background(
                    MaterialTheme.colorScheme.surfaceContainer
                )
        ) {

            LazyColumn {
                items(navigationBarItems, { it.id }) { item ->
                    SelectionImageButton(
                        modifier = Modifier.padding(4.dp),
                        image = item.icon,
                        contentDescription = item.description ?: item.id,
                        selected = selectedTag === item.id,
                        onClick = {
                            item.onClick?.invoke()
                            selectedTag = item.id
                        }
                    )
                }
            }

            for (item in navigationBarItems) {
                val slot = item.slot
                if (item.id === selectedTag && slot != null) {
                    slot.Slot()
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            SelectionImageButton(
                modifier = Modifier.padding(
                    4.dp,
                    4.dp,
                    4.dp,
                    16.dp
                ),
                image =
                Icons.TwoTone.Settings,
                contentDescription = "设置",
                selected = false
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionImageButton(
    modifier: Modifier,
    image: ImageVector,
    contentDescription: String,
    selected: Boolean,
    onClick: () -> Unit = {}
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                Text(text = contentDescription)
            }
        },
        state = rememberTooltipState(initialIsVisible = false)
    ) {
        Box(
            modifier =
            modifier
                .size(52.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainer
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center

        ) {
            Icon(
                image, contentDescription = contentDescription,
                tint = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}