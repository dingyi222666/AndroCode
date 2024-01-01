package io.dingyi222666.rewrite.androlua.api.plugin

import io.dingyi222666.rewrite.androlua.api.common.IDisposable
import io.dingyi222666.rewrite.androlua.api.context.Context

interface ReWriteAndroLuaPlugin {

    suspend fun activate(ctx: Context)

    fun config(): PluginConfig
}

