package io.dingyi222666.androcode.api.plugin


import io.dingyi222666.androcode.annotation.PluginMain
import io.dingyi222666.androcode.api.context.Context

@PluginMain
class Test : AndroCodePlugin {
    override suspend fun activate(ctx: Context) {
        TODO("Not yet implemented")
    }

    override fun config(): PluginConfig {
        TODO("Not yet implemented")
    }
}