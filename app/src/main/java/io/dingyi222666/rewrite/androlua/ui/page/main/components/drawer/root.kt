package io.dingyi222666.rewrite.androlua.ui.page.main.components.drawer

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
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
                NavigationBar()
            }
        },
        content = content,
    )

}
