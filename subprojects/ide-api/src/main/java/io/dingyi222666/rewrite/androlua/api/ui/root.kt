package io.dingyi222666.rewrite.androlua.api.ui

import io.dingyi222666.rewrite.androlua.annotation.AutoGenerateServiceExtension
import io.dingyi222666.rewrite.androlua.annotation.AutoService
import io.dingyi222666.rewrite.androlua.api.AndroLua
import io.dingyi222666.rewrite.androlua.api.configureBase
import io.dingyi222666.rewrite.androlua.api.context.Context
import io.dingyi222666.rewrite.androlua.api.context.Service
import io.dingyi222666.rewrite.androlua.api.context.getAs

class UIService internal constructor(
    override val ctx: Context
) : Context("ui", ctx), Service {

    init {
        configureBase()
    }

    override fun dispose() {
        super<Service>.dispose()
        super<Context>.dispose()
    }
}

@AutoService(Context::class, "ui")
@AutoGenerateServiceExtension(Context::class, "ui", "ui")
fun createUIService(ctx: Context): UIService {
    return ctx.getOrNull("ui", false) ?: UIService(ctx)
}

