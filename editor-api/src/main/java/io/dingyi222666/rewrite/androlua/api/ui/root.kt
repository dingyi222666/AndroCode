package io.dingyi222666.rewrite.androlua.api.ui

import io.dingyi222666.rewrite.androlua.api.service.IServiceRegistry
import io.dingyi222666.rewrite.androlua.api.service.Service
import io.dingyi222666.rewrite.androlua.api.service.getAs

class UIService internal constructor(
    serviceRegistry: IServiceRegistry
) : Service {

    override val name = "ui"

    override val registry: IServiceRegistry = serviceRegistry

    init {
        registry.registerConstructor("ui.navigationBar", ::createNavigationBarService)
    }

    val navigationBar by lazy(LazyThreadSafetyMode.NONE) {
        serviceRegistry.getAs<NavigationBarService>("ui.navigationBar")
    }
}

fun createUIService(serviceRegistry: IServiceRegistry): UIService {
    return UIService(serviceRegistry)
}

