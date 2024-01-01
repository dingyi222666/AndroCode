package io.dingyi222666.androcode.api.plugin

import io.dingyi222666.androcode.api.common.IDisposable
import io.dingyi222666.androcode.api.context.Context

interface ReWriteAndroLuaPlugin {

    suspend fun activate(ctx: Context)

    fun config(): PluginConfig
}

