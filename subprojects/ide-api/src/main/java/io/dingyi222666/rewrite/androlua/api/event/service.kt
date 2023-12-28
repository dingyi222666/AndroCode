package io.dingyi222666.rewrite.androlua.api.event

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

fun createEventService(ctx: Context): EventService {
    val parentService = ctx.parent?.getOrNull<EventService>("event")

    if (parentService != null) {
        return EventService(ctx, parentService)
    }

    if (ctx.root === ctx) {
        return EventService(ctx)
    }

    throw IllegalStateException("EventService not found")
}