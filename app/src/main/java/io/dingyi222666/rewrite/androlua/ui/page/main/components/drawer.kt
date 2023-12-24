package io.dingyi222666.rewrite.androlua.ui.page.main.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Build
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.material.icons.twotone.History
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material.icons.twotone.MyLocation
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet

import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.dingyi222666.rewrite.androlua.MainApplication
import io.dingyi222666.rewrite.androlua.R
import io.dingyi222666.rewrite.androlua.ui.page.common.DismissibleNavigationDrawer
import io.dingyi222666.rewrite.androlua.ui.page.common.DrawerState
import io.dingyi222666.rewrite.androlua.ui.page.common.DrawerValue
import io.dingyi222666.rewrite.androlua.ui.page.common.ModalNavigationDrawer
import io.dingyi222666.rewrite.androlua.ui.page.common.rememberDrawerState
import io.dingyi222666.rewrite.androlua.ui.resource.LocalWindowSizeClass


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDrawer(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    content: @Composable () -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val currentDrawerWidthFraction = when (LocalWindowSizeClass.current.widthSizeClass) {
        WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> 0.85f
        else -> 0.8f
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerWidth = screenWidth * currentDrawerWidthFraction,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(currentDrawerWidthFraction)
                    .fillMaxHeight(),
                drawerShape = RoundedCornerShape(0.dp, 16.dp, 16.dp, 0.dp)
            ) {
                DrawerContent(

                )
            }
        },
        content = content,
    )

}


@Composable
@Preview
fun DrawerContent() {
    val drawerItems = listOf(
        "文件浏览器" to Icons.TwoTone.Folder,
        "导航" to Icons.TwoTone.MyLocation,
        "打包" to Icons.TwoTone.Build
    )

    var selectedTag by rememberSaveable {
        mutableStateOf(drawerItems[0].first)
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

            for (item in drawerItems) {
                SelectionImageButton(
                    modifier = Modifier.padding(4.dp),
                    imageVector = item.second,
                    contentDescription = item.first,
                    selected = selectedTag === item.first,
                    onClick = {
                        selectedTag = item.first
                    }
                )
            }


            Spacer(modifier = Modifier.weight(1f))

            SelectionImageButton(
                modifier = Modifier.padding(
                    4.dp,
                    4.dp,
                    4.dp,
                    16.dp
                ),
                imageVector =
                Icons.TwoTone.Settings,
                contentDescription = "设置",
                selected = false
            )
        }

    }
}

@Composable
fun SelectionImageButton(
    modifier: Modifier,
    imageVector: ImageVector,
    contentDescription: String,
    selected: Boolean,

    onClick: () -> Unit = {}
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
            imageVector, contentDescription = contentDescription,
            tint = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}