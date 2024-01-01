package io.dingyi222666.rewrite.androlua.api.plugin


import io.dingyi222666.rewrite.androlua.annotation.PluginMain
import io.dingyi222666.rewrite.androlua.api.context.Context
import kotlinx.coroutines.withContext

@PluginMain
class Test : ReWriteAndroLuaPlugin {
    override suspend fun activate(ctx: Context) {
        TODO("Not yet implemented")
    }

    override fun config(): PluginConfig {
        TODO("Not yet implemented")
    }
}