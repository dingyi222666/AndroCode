package io.dingyi222666.rewrite.androlua.api

import io.dingyi222666.rewrite.androlua.api.context.Context
import io.dingyi222666.rewrite.androlua.api.context.Service
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

class CoroutineService internal constructor(
    override val ctx: Context
) : Service {

    override val id = "coroutine"

    val rootJob = SupervisorJob()
    val rootCoroutine = CoroutineScope(Dispatchers.IO + rootJob)

    val mainCoroutine = CoroutineScope(Dispatchers.Main)

    override fun dispose() {
        super.dispose()

        rootJob.cancel()
        rootJob.cancelChildren()
        mainCoroutine.coroutineContext.cancelChildren()
    }
}

fun createCoroutineService(registry: Context): CoroutineService {
    return CoroutineService(registry)
}