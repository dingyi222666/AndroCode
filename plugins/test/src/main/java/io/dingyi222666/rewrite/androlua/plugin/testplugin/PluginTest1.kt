package io.dingyi222666.rewrite.androlua.plugin.testplugin

import io.dingyi222666.rewrite.androlua.annotation.PluginMain
import io.dingyi222666.rewrite.androlua.api.context.Context
import io.dingyi222666.rewrite.androlua.api.plugin.PluginConfig
import io.dingyi222666.rewrite.androlua.api.plugin.ReWriteAndroLuaPlugin
import io.dingyi222666.rewrite.androlua.api.plugin.buildPluginConfig

@PluginMain
class PluginTest1 : ReWriteAndroLuaPlugin {
    override suspend fun activate(ctx: Context) {

    }

    override fun config() = buildPluginConfig {
        id = "com.plugin.test1"
        displayName = "test1"
        apiDependencyVersion = 1
        activationEvents {
            event("onLanguage:kotlin")
        }
    }
}