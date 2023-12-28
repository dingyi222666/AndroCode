package io.dingyi222666.rewrite.androlua.api

import io.dingyi222666.rewrite.androlua.api.command.CommandService
import io.dingyi222666.rewrite.androlua.api.command.createCommandService
import io.dingyi222666.rewrite.androlua.api.common.Disposer
import io.dingyi222666.rewrite.androlua.api.common.createDisposer
import io.dingyi222666.rewrite.androlua.api.context.Context

import io.dingyi222666.rewrite.androlua.api.context.getAs
import io.dingyi222666.rewrite.androlua.api.event.EventService
import io.dingyi222666.rewrite.androlua.api.event.createEventService
import io.dingyi222666.rewrite.androlua.api.plugin.Test
import io.dingyi222666.rewrite.androlua.api.ui.UIService
import io.dingyi222666.rewrite.androlua.api.ui.createUIService


object AndroLua : Context("root") {

    init {
        configureBase()
        val s = Test()
    }
}


fun Context.configureBase() {
    registerConstructor("coroutine", ::createCoroutineService)
    registerConstructor("command", ::createCommandService)
    registerConstructor("ui", ::createUIService)
    registerConstructor("disposer", ::createDisposer)
    registerConstructor("event", ::createEventService)
}




