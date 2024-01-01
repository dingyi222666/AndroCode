package io.dingyi222666.androcode.api.ui

import io.dingyi222666.androcode.annotation.AutoGenerateServiceExtension
import io.dingyi222666.androcode.annotation.AutoService
import io.dingyi222666.androcode.api.configureBase
import io.dingyi222666.androcode.api.context.Context
import io.dingyi222666.androcode.api.context.Service

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

