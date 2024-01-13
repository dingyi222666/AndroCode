package io.dingyi222666.androcode.api.plugin

import io.dingyi222666.androcode.api.context.Context
import io.dingyi222666.androcode.api.context.Service
import io.dingyi222666.androcode.api.plugin.loader.ApkClassLoader
import io.dingyi222666.androcode.api.plugin.loader.CombineClassLoader
import io.dingyi222666.androcode.api.plugin.loader.FilteringClassLoader
import java.lang.ClassLoader.getSystemClassLoader

class PluginService(
    override val ctx: Context
) : Service {


    override fun dispose() {
        super.dispose()
    }

    override val id = "plugin"

}

class PluginContainer {

    private val combineClassLoader = CombineClassLoader(emptyArray(), getSystemClassLoader())

    private val plugins = mutableMapOf<String, PluginDescriptor>()


}

data class PluginDescriptor(
    val rawPlugin: ReWriteAndroLuaPlugin,
    val pluginId: String,
    val pluginFilteringClassLoader: FilteringClassLoader,
    val pluginDexClassLoader: ApkClassLoader
)
