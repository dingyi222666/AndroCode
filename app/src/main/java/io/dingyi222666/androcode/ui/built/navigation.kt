package io.dingyi222666.androcode.ui.built

import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Build
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.material.icons.twotone.MyLocation
import io.dingyi222666.androcode.MainActivity
import io.dingyi222666.androcode.MainApplication
import io.dingyi222666.androcode.api.ui.navigationBar
import io.dingyi222666.androcode.api.ui.ui

import io.dingyi222666.androcode.ui.page.main.MainViewModel

fun navigation(activity: MainActivity) {
    /* val drawerItems = listOf(
         "文件浏览器" to Icons.TwoTone.Folder,
         "导航" to Icons.TwoTone.MyLocation,
         "打包" to Icons.TwoTone.Build
     )*/


    val viewModel by activity.viewModels<MainViewModel>()

    val androCode = MainApplication.instance.androCode

    androCode.ui.navigationBar.registerItem(
        id = "file_explorer",
        icon = Icons.TwoTone.Folder,
        description = "文件浏览器",
        onClick = {
            Toast.makeText(activity, "666", Toast.LENGTH_LONG).show()
        }
    )

    androCode.ui.navigationBar.registerItem(
        id = "file2_explorer",
        icon = Icons.TwoTone.MyLocation,
        description = "导航",
        onClick = {
            Toast.makeText(activity, "666", Toast.LENGTH_LONG).show()
        }
    )

    androCode.ui.navigationBar.registerItem(
        id = "file3_explorer",
        icon = Icons.TwoTone.Build,
        description = "打包",
        onClick = {
            Toast.makeText(activity, "666", Toast.LENGTH_LONG).show()
        }
    )

}