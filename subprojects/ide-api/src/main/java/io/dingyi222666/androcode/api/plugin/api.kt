package io.dingyi222666.androcode.api.plugin

import io.dingyi222666.androcode.api.context.Context

interface AndroCodePlugin {

    suspend fun activate(ctx: Context)

    fun config(): PluginConfig
}

