package io.dingyi222666.rewrite.androlua.api.plugin

import io.dingyi222666.rewrite.androlua.api.context.Context
import io.dingyi222666.rewrite.androlua.api.context.Service

class PluginService(
    override val ctx: Context
) : Service {



    override val id = "plugin"

}