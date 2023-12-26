package io.dingyi222666.rewrite.androlua.api.ui

import io.dingyi222666.rewrite.androlua.api.context.Context
import io.dingyi222666.rewrite.androlua.api.context.Service
import io.dingyi222666.rewrite.androlua.api.context.getAs

class UIService internal constructor(
    override val ctx: Context
) : Context("ui", ctx), Service {

    init {
        ctx.registerConstructor("ui.navigationBar", ::createNavigationBarService)
    }

    val navigationBar by lazy(LazyThreadSafetyMode.NONE) {
        getAs<NavigationBarService>("ui.navigationBar")
    }

    override fun dispose() {
        super<Service>.dispose()
        super<Context>.dispose()
    }
}

fun createUIService(ctx: Context): UIService {
    return ctx.getOrNull("ui") ?: UIService(ctx)
}

