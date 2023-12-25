package io.dingyi222666.rewrite.androlua.ui.built

import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Build
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.material.icons.twotone.MyLocation
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import io.dingyi222666.rewrite.androlua.MainActivity
import io.dingyi222666.rewrite.androlua.api.AndroLua
import io.dingyi222666.rewrite.androlua.ui.page.main.MainViewModel
import kotlinx.coroutines.launch

fun navigation(activity: MainActivity) {
    /* val drawerItems = listOf(
         "文件浏览器" to Icons.TwoTone.Folder,
         "导航" to Icons.TwoTone.MyLocation,
         "打包" to Icons.TwoTone.Build
     )*/


    val viewModel by activity.viewModels<MainViewModel>()

    println(AndroLua.ui.navigationBar)
    AndroLua.ui.navigationBar.registerItem(
        id = "file_explorer",
        icon = Icons.TwoTone.Folder,
        description = "文件浏览器",
        onClick = {
            activity.lifecycleScope.launch {
                viewModel.snackbarHostState.showSnackbar("click file explorer")
            }
        }
    )

}