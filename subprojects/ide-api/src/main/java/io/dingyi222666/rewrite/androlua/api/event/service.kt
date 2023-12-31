package io.dingyi222666.rewrite.androlua.api.event

import io.dingyi222666.rewrite.androlua.annotation.AutoGenerateServiceExtension
import io.dingyi222666.rewrite.androlua.annotation.AutoService
import io.dingyi222666.rewrite.androlua.api.AndroLua
import io.dingyi222666.rewrite.androlua.api.context.Context
import io.dingyi222666.rewrite.androlua.api.context.Service

class EventService internal constructor(
    override val ctx: Context,
    parent: EventEmitter? = null
) : EventEmitter(parent), Service {

    override val id = "event"

    override fun dispose() {
        super<Service>.dispose()
        super<EventEmitter>.dispose()
    }
}

@AutoService(Context::class, "event")
@AutoGenerateServiceExtension(Context::class, "event", "event")
fun createEventService(ctx: Context): EventService {
    val parentService = ctx.parent?.getOrNull<EventService>("event", false)

    if (parentService != null) {
        return EventService(ctx, parentService)
    }

    if (ctx.root === ctx) {
        return EventService(ctx)
    }

    throw IllegalStateException("EventService not found")
}