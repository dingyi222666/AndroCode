package io.dingyi222666.androcode.api.plugin

import io.dingyi222666.androcode.api.context.Context
import io.dingyi222666.androcode.api.context.Service

class PluginService(
    override val ctx: Context
) : Service {



    override val id = "plugin"

}