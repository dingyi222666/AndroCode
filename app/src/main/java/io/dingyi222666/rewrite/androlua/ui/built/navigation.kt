package io.dingyi222666.rewrite.androlua.ui.built

import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Build
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.material.icons.twotone.MyLocation
import io.dingyi222666.rewrite.androlua.MainActivity
import io.dingyi222666.rewrite.androlua.api.AndroLua
import io.dingyi222666.rewrite.androlua.api.navigationBar
import io.dingyi222666.rewrite.androlua.api.ui
import io.dingyi222666.rewrite.androlua.ui.page.main.MainViewModel

fun navigation(activity: MainActivity) {
    /* val drawerItems = listOf(
         "文件浏览器" to Icons.TwoTone.Folder,
         "导航" to Icons.TwoTone.MyLocation,
         "打包" to Icons.TwoTone.Build
     )*/


    val viewModel by activity.viewModels<MainViewModel>()


    AndroLua.ui.navigationBar.registerItem(
        id = "file_explorer",
        icon = Icons.TwoTone.Folder,
        description = "文件浏览器",
        onClick = {
            Toast.makeText(activity, "666", Toast.LENGTH_LONG).show()
        }
    )

    AndroLua.ui.navigationBar.registerItem(
        id = "file2_explorer",
        icon = Icons.TwoTone.MyLocation,
        description = "导航",
        onClick = {
            Toast.makeText(activity, "666", Toast.LENGTH_LONG).show()
        }
    )

    AndroLua.ui.navigationBar.registerItem(
        id = "file3_explorer",
        icon = Icons.TwoTone.Build,
        description = "打包",
        onClick = {
            Toast.makeText(activity, "666", Toast.LENGTH_LONG).show()
        }
    )

}