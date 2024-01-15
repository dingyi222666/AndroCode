package io.dingyi222666.androcode.plugin

import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Build
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.material.icons.twotone.MyLocation
import io.dingyi222666.androcode.MainActivity
import io.dingyi222666.androcode.MainApplication
import io.dingyi222666.androcode.annotation.PluginMain
import io.dingyi222666.androcode.api.context.Context
import io.dingyi222666.androcode.api.logger.log
import io.dingyi222666.androcode.api.plugin.AndroCodePlugin
import io.dingyi222666.androcode.api.plugin.buildPluginConfig
import io.dingyi222666.androcode.api.ui.navigationBar
import io.dingyi222666.androcode.api.ui.ui

import io.dingyi222666.androcode.ui.page.main.MainViewModel


@PluginMain
class NavigationTest : AndroCodePlugin {
    override suspend fun activate(ctx: Context) {
        ctx.log.current.info("load plugin navigationTest")

        ctx.ui.navigationBar.registerItem(
            id = "file_explorer",
            icon = Icons.TwoTone.Folder,
            description = "文件浏览器",
            onClick = {
                Toast.makeText(MainApplication.instance, "666", Toast.LENGTH_LONG).show()
            }
        )

        ctx.ui.navigationBar.registerItem(
            id = "file2_explorer",
            icon = Icons.TwoTone.MyLocation,
            description = "导航",
            onClick = {
                Toast.makeText(MainApplication.instance, "666", Toast.LENGTH_LONG).show()
            }
        )

        ctx.ui.navigationBar.registerItem(
            id = "file3_explorer",
            icon = Icons.TwoTone.Build,
            description = "打包",
            onClick = {
                Toast.makeText(MainApplication.instance, "666", Toast.LENGTH_LONG).show()
            }
        )

    }

    override fun config() = buildPluginConfig {
        packageName = "system.navigation"
        displayName = "navigation"
        version = "1.0.0"
        sdkVersion = 1
        activationEvents {
            event("*")
        }
    }
}


